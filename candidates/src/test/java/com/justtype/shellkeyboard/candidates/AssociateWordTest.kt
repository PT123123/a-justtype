package com.justtype.shellkeyboard.candidates

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AssociateWordTest {

    private lateinit var associateWord: AssociateWord

    @Before
    fun setUp() {
        associateWord = AssociateWord()
    }

    @Test
    fun initially_notAssociating() {
        assertFalse(associateWord.isAssociating())
    }

    @Test
    fun initially_noAssociatedWords() {
        assertTrue(associateWord.getAssociatedWords().isEmpty())
    }

    @Test
    fun startAssociation_enablesAssociating() {
        associateWord.startAssociation("test")
        assertTrue(associateWord.isAssociating())
    }

    @Test
    fun stopAssociation_disablesAssociating() {
        associateWord.startAssociation("test")
        associateWord.stopAssociation()
        assertFalse(associateWord.isAssociating())
        assertTrue(associateWord.getAssociatedWords().isEmpty())
    }

    @Test
    fun getAssociatedWords_notAssociating_returnsEmpty() {
        assertTrue(associateWord.getAssociatedWords().isEmpty())
    }
}
