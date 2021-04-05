package main

import (
	"fmt"
	"math/rand"
	"time"
)

const NumOfCigarettesAllowed = 20

type Ingredient int

const (
	TOBACCO Ingredient = iota
	PAPER
	MATCHES
)

func (ingredient Ingredient) String() string {
	ingredients := []string{
		"Tobacco",
		"Paper",
		"Matches",
	}

	return ingredients[ingredient]
}


type Moderator struct {
}

func (Moderator) giveItems(partsChan chan Ingredient, smokerChan chan bool, moderatorChan chan bool, endChan chan bool) {
	for i := 0; i < NumOfCigarettesAllowed; i++ {
		if i != 0 {
			<-moderatorChan
		}
		random := rand.New(rand.NewSource(time.Now().UnixNano()))

		var firstIngredient = random.Intn(3)
		var secondIngredient = random.Intn(3)

		for secondIngredient == firstIngredient {
			secondIngredient = random.Intn(3)
		}

		fmt.Printf("Moderator is sending %s and %s\n", Ingredient(firstIngredient), Ingredient(secondIngredient))
		partsChan <- Ingredient(firstIngredient)
		partsChan <- Ingredient(secondIngredient)
		smokerChan <- true
	}
	<-moderatorChan
	smokerChan <- false
	smokerChan <- false
	smokerChan <- false
	endChan <- true
}


type Smoker struct {
	name       string
	ingredient Ingredient
}

func (smoker Smoker) tryToSmoke(partsChan chan Ingredient, smokerChan chan bool, moderatorChan chan bool) {
	for {
		if !<-smokerChan {
			return
		}
		firstIngredient := <-partsChan
		secondIngredient := <-partsChan

		if firstIngredient != smoker.ingredient && secondIngredient != smoker.ingredient {
			fmt.Printf("Smoker %s has got ingredients: %s, %s\n", smoker.name, firstIngredient, secondIngredient)

			fmt.Printf("Smoker %s is adding ingridient %s\n", smoker.name, smoker.ingredient)
			time.Sleep(10 * time.Millisecond)

			fmt.Printf("Smoker %s has smoked a cigarette\n\n", smoker.name)

			moderatorChan <- true
		} else {
			partsChan <- firstIngredient
			partsChan <- secondIngredient
			smokerChan <- true
		}
	}
}

func main() {
	smoker1 := Smoker{"Ivanov", Ingredient(0)}
	smoker2 := Smoker{"Petrov", Ingredient(1)}
	smoker3 := Smoker{"Sydorov", Ingredient(2)}

	partsChan := make(chan Ingredient, 2)
	smokerChan := make(chan bool, 1)
	moderatorChan := make(chan bool, 1)
	endChan := make(chan bool, 1)

	go Moderator{}.giveItems(partsChan, smokerChan, moderatorChan, endChan)
	go smoker1.tryToSmoke(partsChan, smokerChan, moderatorChan)
	go smoker2.tryToSmoke(partsChan, smokerChan, moderatorChan)
	go smoker3.tryToSmoke(partsChan, smokerChan, moderatorChan)

	<-endChan
}
