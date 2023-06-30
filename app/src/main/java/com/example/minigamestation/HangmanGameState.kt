package com.example.minigamestation

sealed class HangmanGameState {
    class Running(val lettersUsed: String,
                  val underscoreWord: String,
                  val drawable: Int) : HangmanGameState()
    class Lost(val wordToGuess: String) : HangmanGameState()
    class Won(val wordToGuess: String) : HangmanGameState()
}
