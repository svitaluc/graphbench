def n = 10_000_000
def namesFileName = 'names.csv'
def friendsFileName = 'friends.csv'

// Define part of names
def p = 'and marc son bert wick ness ton shire step ley ing sley hey bley hay hoy roy roi soi boi soy hey rey day bay lay'.split()
def q = 'Lord Lady Viscount Baronet Marquis Sir Captain Admiral'.split()

// Objects
Set<String> uniqueNames = new HashSet<>()
Random random = new Random()

n.times {
    uniqueNames.add(q[
            random.nextInt(q.size())] + ' ' +
            (0..random.nextInt(3)).collect { p[random.nextInt(p.size())] }.join('').capitalize() + ' ' +
            (0..random.nextInt(7)).collect { p[random.nextInt(p.size())] }.join('').capitalize())

    if (it % 100000 == 0) {
        print "Generating: " + it + "\n"
    }
}

// List for getting friends
List<String> namesList = new ArrayList<>(uniqueNames)

// Write to the file
def namesWriter = new File(namesFileName).newWriter()
def friendsWriter = new File(friendsFileName).newWriter()

// CSV header
namesWriter.writeLine("personId:ID,name,year:int,:LABEL")
friendsWriter.writeLine(":START_ID,:END_ID,:TYPE")
def i = 0
for (String startName : uniqueNames) {
    int year = random.nextInt(2050 + 1 - 1900) + 1900
    def startId = startName.replace(" ", "_").toLowerCase()

    def endName = namesList[random.nextInt(namesList.size())]
    def endId = endName.replace(" ", "_").toLowerCase()

    namesWriter.writeLine(startId + "," + startName + "," + year + "," + "PERSON")
    friendsWriter.writeLine(startId + "," + endId + "," + "FRIEND")

    if (i % 100000 == 0) {
        print "Writing: " + i + "\n"
    }

    i++
}
namesWriter.close()
friendsWriter.close()