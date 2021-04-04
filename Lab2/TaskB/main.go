package main

import (
	"fmt"
	"time"
)

type Soldier struct {
	name string
}

type PropertyItem struct {
	price float32
}

type Warehouse []PropertyItem


const TimeDelay = time.Microsecond


func (soldier Soldier) takeOut(wh Warehouse, itemToLoad chan PropertyItem) {
	for _, item := range wh {
		itemToLoad <- item
		fmt.Printf("%s has taken item from warehouse\n", soldier.name)

		time.Sleep(TimeDelay)
	}
}

func (soldier Soldier) load(itemToLoad chan PropertyItem, itemToCountPrice chan PropertyItem) {
	for i := 0; i < cap(itemToCountPrice); i++ {
		propertyItem := <- itemToLoad
		fmt.Printf("%s has got item from warehouse\n", soldier.name)

		itemToCountPrice <- propertyItem
		fmt.Printf("%s has loaded item to truck\n", soldier.name)

		time.Sleep(TimeDelay)
	}

}

func (soldier Soldier) count(itemToCountPrice chan PropertyItem, output chan float32) {
	var totalPrice float32 = 0.
	for i := 0; i < cap(itemToCountPrice); i++ {
		propertyItem := <-itemToCountPrice
		fmt.Printf("%s is counting price...\n", soldier.name)
		totalPrice += propertyItem.price

		time.Sleep(TimeDelay)
	}

	output <- totalPrice
}

func main() {
	var warehouse = Warehouse{
		{3221}, {131.99}, {111.54}, {41}, {12},
		{189}, {19}, {31.99}, {799.21}, {1000},
	}

	var output = make(chan float32)
	var itemToLoad = make(chan PropertyItem, len(warehouse))
	var itemToCountPrice = make(chan PropertyItem, cap(itemToLoad))

	ivanov := Soldier{"Ivanov"}
	petrov := Soldier{"Petrov"}
	nechyporuk := Soldier{"Nechyporuk"}

	go ivanov.takeOut(warehouse, itemToLoad)
	go petrov.load(itemToLoad, itemToCountPrice)
	go nechyporuk.count(itemToCountPrice, output)

	fmt.Printf("Total price: %.2f\n", <-output)
}
