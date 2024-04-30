class Archive (var name: String , val notes: MutableList<Note> = mutableListOf()): Menu()

val archivesList = mutableListOf<Archive>()

lateinit var archive: Archive    //можно также использовать nullable-type

fun createArchive(){
    println("Введите название архива:")

    archive = Archive(Menu.getUserInput())
    archivesList.add(archive)
}