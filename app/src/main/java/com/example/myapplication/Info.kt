package com.example.myapplication

/**
 * Created by Christoforos Filippou on 02/08/2020.
 * Copyright © 2020 ABN AMRO. All rights reserved.
 *
 */

data class Info(val text: String, var isSelected: Boolean)

sealed class MultiInfo {
    data class Header(val title: String) : MultiInfo()
    data class DataInfo(val text: String, var isSelected: Boolean)
}