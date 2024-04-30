import kotlin.system.exitProcess

open class Menu {

    // класс состояний экрана
    enum class ScreenState{
        ARCHIVE,
        NOTE,
        CONTENT_NOTE
    }

    //глоб перем тк меняем состояние в нескольких ф-х
    private var stateFlag: ScreenState = ScreenState.ARCHIVE

/*    Основная функция меню
    реализовано с помощью состояний*/
    fun startMainLogic()
    {

        val arrStrArchive: Array<String> = arrayOf("архивов","архив")
        val arrStrNote: Array<String> = arrayOf("заметок","заметку")
        var chosenItem: Int? = null

        while(true){
            when(stateFlag){

                ScreenState.ARCHIVE -> {
                    showScreenMenu(arrStrArchive,archivesList)
                    chosenItem = chooseAndCreatePoint(archivesList,::createArchive,{list,chosenPoint -> chooseArchive(list , chosenPoint)})
                }
                ScreenState.NOTE -> {
                    showScreenMenu(arrStrNote,archivesList[(chosenItem ?: 0) - 1].notes)
                    chooseAndCreatePoint(archivesList[(chosenItem ?: 0) - 1].notes,::createNote,{list,chosenPoint -> chooseNote(list, chosenPoint)})
                }
                ScreenState.CONTENT_NOTE ->{
                    println("Для выхода нажмите любую клавишу")
                    scanner.nextLine()
                    stateFlag = ScreenState.NOTE
                }
            }
        }
    }

    //внутренняя функция отрисовки экрана меню
    private fun <T: Any> showScreenMenu(title: Array<String>,lists: MutableList<T>){
        println("Список  ${title[0]}")
        println("0. Создать ${title[1]}")

        //если список не пустой выводим списки архивов/заметок
        if (lists.isNotEmpty()){
            for ((j, spisok) in lists.withIndex()) {
                val obj = when (spisok) {
                    is Note -> spisok.name
                    is Archive -> spisok.name
                    else -> "Type error"
                }
                println("${j+1}. $obj")
            }
        }
        println("${lists.size+1}. Выход")
    }

    //внутренняя функция выбора и отработки пункта меню
    private inline fun <reified T: Any>chooseAndCreatePoint(lists: MutableList<T>, create: ()->Unit, choose: (list: MutableList<T>, chosenPoint: Int)->Unit): Int{
        val countPoints = lists.size

        println("Выберите пункт: ")
        val item = getUserNumber()

        when (item){
            //создание списка архивов/заметок
            0 -> create()
            //выбор пункта меню из списка
            in 1..countPoints -> choose(lists,item)

            //Обработка выхода
            countPoints + 1 -> {

                when (lists.firstOrNull()) {
                    is Note -> stateFlag = ScreenState.ARCHIVE
                    else -> exitProcess(0)
                }
            }
            else -> println("Нет такого пункта ")
        }
//вернуть выбр пункт нужно для сохр выбранного эл-та меню архива для работы с экраном заметок в меню
        return item
    }

    //обработка выбора в экране архивов
    //list - не используется, но нужен для универсальности функции
    private fun chooseArchive(list: MutableList<Archive>,chosenPoint: Int){
        println("Архив: ${archivesList[chosenPoint-1].name}")
        stateFlag = ScreenState.NOTE
    }

    //обработка выбора в экране заметок + печать заметки
    private fun chooseNote(list: MutableList<Note>,chosenPoint: Int){
        println("Заметка: ${list[chosenPoint-1].name}")
        stateFlag = ScreenState.CONTENT_NOTE
        //***********************************
        for(str in list[chosenPoint-1].strings)
            println(" $str")
    }

    //*Ввод пункта меню с проверками
    fun getUserNumber(): Int {

        while (true) {
            when {
                scanner.hasNextInt() -> {
                    val number = scanner.nextInt()
                    //если введено не только одно число
                    if (scanner.hasNextLine()){
                        val remainedData = scanner.nextLine()
                        if (remainedData.length > 1) {
                            println("Введите только число! Введите еще раз:")
                            continue
                        }
                    }
                    return number
                }
                scanner.hasNextDouble() -> {
                    println("Число должно быть целым! Введите еще раз: ")
                    scanner.next()
                }
                else -> {
                    println("Введите только число! Введите еще раз: ")
                    scanner.nextLine()
                }
            }
        }
    }

    companion object {  //чтобы не создавать объект Menu при использовании в Archive и Note
        fun getUserInput(): String {
            var isContinueLoop: Boolean
            var input: String
            do {
                input = scanner.nextLine()

                isContinueLoop = if (input.isBlank()) {
                    println("Вы не можете ввести пустой текст! Введите еще раз ")
                    true
                } else false

            } while (isContinueLoop)
            return input
        }
    }
}