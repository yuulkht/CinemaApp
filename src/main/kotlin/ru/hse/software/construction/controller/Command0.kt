package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.view.ConsoleOutputHandler

class Command0 (
    private val outputHandler: ConsoleOutputHandler = ConsoleOutputHandler()
): Command {
    override fun process(programInfo: ProgramInfo) {
        outputHandler.displayCommands()
    }
}