package main

import (
	"fmt"
	"math/rand"
	"os"
	"sync"
	"time"
)

const ActionsAllowedPerThread = 20
const PauseTime = 100 * time.Millisecond

func boolToFlower(b bool) string {
	if b {
		return "⚘"
	}
	return "."
}

func simulateGardenerWatering(garden [][]bool, rwMutex *sync.RWMutex, exitChan chan int) {
	for i := 0; i < ActionsAllowedPerThread; i++ {
		rwMutex.Lock()
		for i := 0; i < len(garden); i++ {
			for j := 0; j < len(garden[0]); j++ {
				if garden[i][j] == false {
					garden[i][j] = true
				}
			}
		}
		rwMutex.Unlock()
		time.Sleep(PauseTime)
	}
	exitChan <- 1
}

func simulateNaturalChanges(garden [][]bool, rwMutex *sync.RWMutex, exitChan chan int) {
	random := rand.New(rand.NewSource(time.Now().UnixNano()))

	for i := 0; i < ActionsAllowedPerThread; i++ {
		rwMutex.Lock()
		for i := 0; i < len(garden) * 2; i++ {
			index1 := random.Intn(len(garden))
			index2 := random.Intn(len(garden[0]))
			garden[index1][index2] = !garden[index1][index2]
		}
		rwMutex.Unlock()
		time.Sleep(PauseTime)
	}
	exitChan <- 1
}

func writeStatusToFile(garden [][]bool, rwMutex *sync.RWMutex, exitChan chan int) {
	file, err := os.Create("garden-status.txt")

	if err != nil {
		fmt.Println("Error while creating file:", err)
		return
	}

	defer file.Close()

	for i := 0; i < ActionsAllowedPerThread; i++ {
		rwMutex.RLock()
		for i := 0; i < len(garden); i++ {
			var line string
			for j := 0; j < len(garden[0]); j++ {
				line += boolToFlower(garden[i][j]) + " "
			}
			_, _ = file.WriteString(line + "\n")
		}
		rwMutex.RUnlock()
		_, _ = file.WriteString("\n\n")
		time.Sleep(PauseTime)
	}
	exitChan <- 1
}

func printStatusToConsole(garden [][]bool, rwMutex *sync.RWMutex, exitChan chan int) {
	for i := 0; i < ActionsAllowedPerThread; i++ {
		rwMutex.RLock()
		for i := 0; i < len(garden); i++ {
			var line string
			for j := 0; j < len(garden[0]); j++ {
				line += boolToFlower(garden[i][j]) + " "
			}
			fmt.Println(line)
		}
		rwMutex.RUnlock()
		fmt.Print("\n\n")
		time.Sleep(PauseTime)
	}
	exitChan <- 1
}

func main() {
	var garden [][]bool
	var rwMutex sync.RWMutex
	exitChan := make(chan int, 4)

	random := rand.New(rand.NewSource(time.Now().UnixNano()))

	for i := 0; i < 10; i++ {
		var row []bool
		for j := 0; j < 10; j++ {
			row = append(row, random.Intn(2) != 0)
		}
		garden = append(garden, row)
	}

	go printStatusToConsole(garden, &rwMutex, exitChan)
	go writeStatusToFile(garden, &rwMutex, exitChan)
	go simulateNaturalChanges(garden, &rwMutex, exitChan)
	go simulateGardenerWatering(garden, &rwMutex, exitChan)

	for i := 0; i < 4; i++ {
		<-exitChan
	}
}
