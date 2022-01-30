package com.example.jetpackcomposetipcalculator.utils

fun calculateTip(totalBillAmount: Double, tipPercentage: Double): Double {
    return if(totalBillAmount > 1 && totalBillAmount.toString().isNotEmpty()) {
        return ((totalBillAmount * 100.0)/100.0) * ((tipPercentage * 100.0)/100.0)
    } else 0.0
}

fun calculateTotalBillPerPerson(totalBillAmount: Double, tipPercentage: Double, numOfPeople: Int): Double{
    val bill = calculateTip(totalBillAmount = totalBillAmount, tipPercentage = tipPercentage) + totalBillAmount
    return bill / numOfPeople
}