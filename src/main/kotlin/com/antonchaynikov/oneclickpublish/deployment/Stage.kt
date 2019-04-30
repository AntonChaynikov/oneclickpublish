package com.antonchaynikov.oneclickpublish.deployment

interface Stage {
    fun execute()
    fun revert()
}