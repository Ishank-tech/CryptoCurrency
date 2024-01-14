package com.example.cryptocurrency.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    TestRule, TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun apply(
        base: org.junit.runners.model.Statement,
        description: Description
    ): org.junit.runners.model.Statement {
        return object : org.junit.runners.model.Statement() {
            override fun evaluate() {
                Dispatchers.setMain(dispatcher)

                try {
                    base.evaluate()
                } finally {
                    Dispatchers.resetMain()
                    cleanupTestCoroutines()
                }
            }
        }
    }
}
