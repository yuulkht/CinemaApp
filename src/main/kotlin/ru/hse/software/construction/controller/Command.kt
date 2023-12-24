package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo

interface Command {
    fun process(programInfo : ProgramInfo)
}