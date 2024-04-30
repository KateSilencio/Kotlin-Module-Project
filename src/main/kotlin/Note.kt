class Note (val name: String, val strings: MutableList<String> = mutableListOf()): Menu()

lateinit var note: Note

        fun createNote() {
            println(" Введите имя заметки: ")

            note = Note(Menu.getUserInput())
            do {
                println("Введите текст заметки: ")

                var text = Menu.getUserInput()
                note.strings.add(text)

                println("Добавить следующую строку? (для продолжения введите yes) ")
                text = scanner.nextLine()

            } while (text.trim().lowercase().equals("yes"))
            archive.notes.add(note)
        }
