package main

import (
	"fmt"
	"math"
	"math/rand"
	"sync"
)

const ArraySize = 4
var arrays = [][]int{{3, 6, 3, 20}, {2, 9, 4, -1}, {-8, 3, -2, 0}}
var sums = []int{0, 0, 0}


type CyclicBarrier struct {
	phase   int
	count   int
	parties int
	trigger *sync.Cond
}

func (b *CyclicBarrier) nextGeneration() {
	b.trigger.Broadcast()
	b.count = b.parties
	b.phase++
}

func (b *CyclicBarrier) await() {
	b.trigger.L.Lock()
	defer b.trigger.L.Unlock()
	phase := b.phase
	b.count--

	if b.count == 0 {
		b.nextGeneration()
	} else {
		for phase == b.phase {
			b.trigger.Wait()
		}
	}
}


func getSumDiff(arrI int) int{
	i, j := 1, 2

	if arrI == 1 {
		i = 0
	}
	if arrI == 2 {
		j = 0
	}

	if sums[arrI] == sums[i] || sums[arrI] == sums[j] {
		return 0
	}

	sumIncrDiff := math.Abs(float64(sums[arrI] + 1 - sums[i])) + math.Abs(float64(sums[arrI] + 1 - sums[j]))
	sumDecrDiff := math.Abs(float64(sums[arrI] - 1 - sums[i])) + math.Abs(float64(sums[arrI] - 1 - sums[j]))

	if sumIncrDiff <= sumDecrDiff {
		return 1
	}
	return -1
}


func changeArraySum(arrayIndex int, b *CyclicBarrier, endChan chan bool) {
	for {
		sums[arrayIndex] = 0
		for i := 0; i < ArraySize; i++ {
			sums[arrayIndex] += arrays[arrayIndex][i]
		}

		b.await()

		if sums[0] == sums[1] && sums[1] == sums[2] {
			fmt.Printf("Finished, sum = %d\n", sums[arrayIndex])
			break
		} else {
			fmt.Printf("Current sums: %d,%d,%d\n", sums[0], sums[1], sums[2])
		}
		b.await()

		diff := getSumDiff(arrayIndex)
		arrays[arrayIndex][rand.Intn(ArraySize)] += diff
	}
	endChan <- true
}

func main() {
	cyclicBarrier := CyclicBarrier{count: 3, parties: 3, trigger: sync.NewCond(&sync.Mutex{})}

	endChan := make(chan bool, 3)

	go changeArraySum(0, &cyclicBarrier, endChan)
	go changeArraySum(1, &cyclicBarrier, endChan)
	go changeArraySum(2, &cyclicBarrier, endChan)

	for i := 0; i < 3; i++ {
		<- endChan
		fmt.Println(arrays[i], sums[i])
	}

}
