package com.maxli.coursegpa

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maxli.coursegpa.ui.theme.CourseTheme

private val validGrades = setOf(
    "A", "A-",
    "B+", "B", "B-",
    "C+", "C", "C-",
    "D+", "D", "D-",
    "F"
)

data class FameItem(val title: String, val detail: String, val imageRes: Int? = null)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val owner = LocalViewModelStoreOwner.current
                    owner?.let {
                        val viewModel: MainViewModel = viewModel(
                            it,
                            "MainViewModel",
                            MainViewModelFactory(
                                LocalContext.current.applicationContext as Application
                            )
                        )
                        TabScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TabScreen(viewModel: MainViewModel) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Acad", "Trivial", "Fame", "Dining")

    Column(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.weight(1f)) {
            when (tabIndex) {
                0 -> ScreenSetup(viewModel)
                1 -> TrivialScreen(viewModel)
                2 -> FameScreen()
                3 -> DiningScreen()
            }
        }

        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = MaterialTheme.colorScheme.secondary,   // background of tab row
            contentColor = MaterialTheme.colorScheme.onSecondary    // default text color
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(title) },
                    selectedContentColor = MaterialTheme.colorScheme.tertiary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun FameScreen() {
    val nationalRankings = listOf(
        FameItem("Best Regional University", "Roger Williams University is placed 35th as the best regional university in the North. This ranking highlights our commitment to providing a top-tier education and a supportive learning environment for all students."),
        FameItem("Best Schools for Veterans", "Ranked 47th for best schools for veterans. We take great pride in supporting our service members with dedicated resources, flexible programs, and a welcoming community that honors their service."),
        FameItem("Architecture Excellence", "The Architecture program is ranked 78th in the nation. Our Cummings School of Architecture is renowned for its innovative curriculum and for preparing students to lead in a rapidly changing world."),
        FameItem("Best Campus Food", "Our food is ranked 21st in the nation! From locally sourced ingredients to diverse culinary options, our dining services consistently deliver a high-quality experience for the entire campus community.")
    )

    val athleticAchievements = listOf(
        FameItem("CCC Team Champions", "Mens track & field 2024 CCC team Champions scoring 252 points. This victory showcases the incredible talent and dedication of our student-athletes and coaching staff in achieving excellence on the field."),
        FameItem("First 2,000-Point Scorer", "Mens basketball player Ian Coene became the first 2,000-point scorer in program history. This historic milestone reflects years of hard work, skill, and a passion for the game that inspires us all."),
        FameItem("Records Broken", "Womans basketball player Katy Bovee set records with 1,570 career points and 178 three-pointers. Her outstanding performance on the court has cemented her legacy as one of the program's all-time greats."),
        FameItem("National Sailing Champions", "Sailing team won the 2024 ICSA Open Team Race National Championship. This achievement solidifies Roger Williams University's position as a powerhouse in collegiate sailing on a national stage.")
    )

    val alumni = listOf(
        FameItem("Tim Baxter", "Tim Baxter served as the CEO of Samsung Electronics North America. His leadership in the global tech industry is a testament to the strong foundation and leadership skills developed during his time at RWU.", R.drawable.tim_baxter),
        FameItem("Chris Sparling", "Chris Sparling is a successful screenwriter and director, known for his work on films like 'Buried'. His creative contributions to the film industry bring great pride to his alma mater.", R.drawable.chris_sparling),
        FameItem("Jerry Remy", "Jerry Remy was a former MLB player and a beloved broadcaster for the Boston Red Sox. Known as 'RemDawg', his legacy in the world of sports and media continues to inspire fans and students alike.", R.drawable.jerry_remy),
        FameItem("James W. Nuttall", "James W. Nuttall is a retired United States Army Major General. His distinguished career in military service exemplifies the values of leadership, duty, and honor that we strive to instill in all RWU students.", R.drawable.james_nuttall),
        FameItem("Joe Polisena", "Joe Polisena is a former Rhode Island State Senator and served as the mayor of Johnston, RI. His dedication to public service and community development has had a lasting impact on the region.", R.drawable.joe_polesina),
        FameItem("Peter Kilmartin", "Peter Kilmartin served as the 73rd Attorney General of Rhode Island. His commitment to justice and public safety throughout his career reflects the high standards of our alumni in the legal profession.", R.drawable.peter_kilmartin1)
    )

    val braggingRights = listOf(
        FameItem("Beautiful Bristol Campus", "Stunning waterfront views on our historic 140-acre campus. Our location in Bristol, Rhode Island, provides an inspiring backdrop for learning, living, and creating lifelong memories."),
        FameItem("Global Sustainability", "Top 5% of universities globally for sustainability initiatives. We are committed to environmental stewardship and integrating sustainable practices into every aspect of campus life."),
        FameItem("Student Engagement", "Ranked 1st in Rhode Island for student engagement and success. Our faculty and staff are dedicated to ensuring every student has the support they need to thrive academically and personally.")
    )

    var selectedItem by remember { mutableStateOf<FameItem?>(null) }

    if (selectedItem != null) {
        AlertDialog(
            onDismissRequest = { selectedItem = null },
            title = { Text(selectedItem!!.title, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (selectedItem!!.imageRes != null) {
                        Image(
                            painter = painterResource(id = selectedItem!!.imageRes!!),
                            contentDescription = selectedItem!!.title,
                            modifier = Modifier
                                .size(200.dp)
                                .padding(bottom = 16.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Text(
                        text = selectedItem!!.detail,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedItem = null }) {
                    Text("Close", color = MaterialTheme.colorScheme.secondary)
                }
            },
            containerColor = Color.White,
            textContentColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.secondary
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Logo and Header
        HeaderSection("Famous People & Accomplishments")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item { FameHeader("National Rankings") }
            items(nationalRankings) { item -> FameCard(item) { selectedItem = it } }

            item { FameHeader("Athletic Achievements") }
            items(athleticAchievements) { item -> FameCard(item) { selectedItem = it } }

            item { FameHeader("Notable Alumni & People") }
            items(alumni) { item -> FameCard(item) { selectedItem = it } }

            item { FameHeader("More Bragging Rights") }
            items(braggingRights) { item -> FameCard(item) { selectedItem = it } }
        }
    }
}

@Composable
fun FameHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun FameCard(item: FameItem, onClick: (FameItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item) },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6E6FA) // Light Lavender
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.title,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                contentDescription = "Details",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun DiningScreen() {
    val diningHalls = listOf(
        FameItem("Upper Commons", "The Upper Commons is the University’s main dining hall serving breakfast, lunch, and dinner. Choices seem endless at this all-you-care-to-eat café — from made-to-order stir-fry, fresh salads, comforting entrée’s, to tempting pizza, pasta, and grill favorites. The Upper Commons also has a dedicated made-without-gluten station and made-without-gluten options at all stations."),
        FameItem("Lower Commons", "The Lower Commons is located right below the Upper Commons and has everything from GO meals to sushi, pizza, grill, a salad bar, and made-to-order sandwiches – fire grilled or cold!"),
        FameItem("Roger's Café", "The Roger's Café is a taqueria-inspired café serving breakfast and lunch along with GO meals, soup, and sushi."),
        FameItem("Hawk's Nest", "The Hawk’s Nest is located in the Recreation Center and serves coffee, fresh pastries, and bagels in addition to fruit smoothies and GO meals."),
        FameItem("Global Café", "Global Café serves Starbucks coffee and specializes in espresso drinks, but also GO meals, grilled sandwiches, and breakfast sandwiches."),
        FameItem("Just Baked", "Just Baked kiosk is located in Center for Student Development offers 24hr convenient hot meal and snacks.")
    )

    val mealPlans = listOf(
        FameItem("125 Block Plan", "Ideal plan for apartment dwellers who want to eat an average of 7-8 meals per week on campus and use 400 in Hawk Dollars throughout the semester; 3 Bonus Meals per semester to be used for yourself, another on-campus student, or commuter."),
        FameItem("Block Meal Plans", "Block Meal Plans offer students the most flexibility as the amount of meals can be used throughout the semester in any manner. Students can use their block meals at our two all-you-care-to-eat dining halls.  All Block Meal Plans come with Bonus Meals, these meals can be used for guests not on a Roger Williams dining plan."),
        FameItem("Commuter Plan", "This plan offers the best value for commuting students. You will receive 20 meals per semester that can be eaten at any meal period, as well as 300 Hawk Dollars."),
        FameItem("Hawk Dollars", "Hawk Dollars are accepted like cash in all our dining locations.  Hawk Dollars are a “declining balance account” that works on the same principle as a debit card.  You can use your Hawk Dollars to purchase beverages, snacks, or even a full meal in all of the RWU retail and residential locations."),
        FameItem("Points", "Much like Hawk Dollars, Points are accepted like cash in all our dining locations and are a “declining balance account” that works on the same principle as a debit card ")
    )

    var selectedItem by remember { mutableStateOf<FameItem?>(null) }

    if (selectedItem != null) {
        AlertDialog(
            onDismissRequest = { selectedItem = null },
            title = { Text(selectedItem!!.title, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = selectedItem!!.detail,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = { selectedItem = null }) {
                    Text("Close", color = MaterialTheme.colorScheme.secondary)
                }
            },
            containerColor = Color.White,
            textContentColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.secondary
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderSection("Dining Services")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item { FameHeader("Dining Locations") }
            items(diningHalls) { hall ->
                FameCard(hall) { selectedItem = it }
            }

            item { FameHeader("Meal Plan Options") }
            items(mealPlans) { plan ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4E6)) // Light Orange
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = plan.title,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = plan.detail,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrivialScreen(viewModel: MainViewModel) {
    val allQuestions by viewModel.allTrivialQuestions.observeAsState(listOf())
    var numberOfQuestions by remember { mutableStateOf("") }
    var questions by remember { mutableStateOf<List<TrivialQuestion>>(emptyList()) }
    val (selectedAnswers, setSelectedAnswers) = remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var score by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    // Flag to handle the case where we just clicked Load but DB was empty or being refreshed
    var waitingForLoad by remember { mutableStateOf(false) }

    LaunchedEffect(allQuestions) {
        // If we were waiting for a load, and we now have a full set (or at least some data)
        if (waitingForLoad && allQuestions.isNotEmpty()) {
            questions = allQuestions.shuffled()
            waitingForLoad = false
        }
    }

    val navyButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    )
    val orangeButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        HeaderSection("University Trivia")


        Spacer(modifier = Modifier.height(16.dp))

        // Load Button
        Button(
            onClick = {
                viewModel.loadTriviaQuestions()
                // Reset state for a new game
                setSelectedAnswers(emptyMap())
                score = null
                isError = false
                
                if (allQuestions.isNotEmpty()) {
                    questions = allQuestions.shuffled()
                } else {
                    waitingForLoad = true
                }
            },
            colors = navyButtonColors,
            modifier = Modifier.width(120.dp)
        ) {
            Text("Load", fontSize = 18.sp)
        }

        // Input Section
        CustomTextField(
            title = "Number of Questions",
            textState = numberOfQuestions,
            onTextChange = {
                numberOfQuestions = it
                isError = false
            },
            keyboardType = KeyboardType.Number,
            isError = isError,
            errorMessage = "Enter a number between 1 and 10"
        )

        // Go Button
        Button(
            onClick = {
                val num = numberOfQuestions.toIntOrNull()
                if (num != null && num > 0 && num <= 10) {
                    isError = false
                    // Ensure we have questions to take from
                    if (allQuestions.isNotEmpty()) {
                        questions = allQuestions.shuffled().take(num)
                    } else {
                        // If user hasn't pressed Load yet, maybe load them first?
                        viewModel.loadTriviaQuestions()
                        waitingForLoad = true // This will load all 10, then they can 'Go' again or we can handle it
                    }
                    setSelectedAnswers(emptyMap())
                    score = null
                } else {
                    isError = true
                }
            },
            colors = orangeButtonColors,
            modifier = Modifier.width(100.dp)
        ) {
            Text("Go", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Questions List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(questions) { question ->
                QuestionCard(
                    question = question,
                    selectedAnswer = selectedAnswers[question.id],
                    onAnswerSelected = {
                        val newAnswers = selectedAnswers.toMutableMap()
                        newAnswers[question.id] = it
                        setSelectedAnswers(newAnswers)
                    }
                )
            }
        }

        // Grade Button
        if (questions.isNotEmpty()) {
            Button(
                onClick = {
                    var correct = 0
                    questions.forEach { question ->
                        if (selectedAnswers[question.id] == question.correctAnswer) {
                            correct++
                        }
                    }
                    score = "$correct/${questions.size}"
                },
                enabled = selectedAnswers.size == questions.size,
                colors = orangeButtonColors,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .width(130.dp)
            ) {
                Text("Grade", fontSize = 18.sp)
            }
        }

        // Score Section
        score?.let {
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Your Score",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun QuestionCard(question: TrivialQuestion, selectedAnswer: String?, onAnswerSelected: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6E6FA) // Light Lavender
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = question.questionName,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            val choices = listOf("A", "B", "C", "D")
            val choiceText = listOf(question.choiceA, question.choiceB, question.choiceC, question.choiceD)

            choices.forEachIndexed { index, choice ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (selectedAnswer == choice),
                            onClick = { onAnswerSelected(choice) } )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedAnswer == choice),
                        onClick = { onAnswerSelected(choice) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.tertiary,
                            unselectedColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                    Text(
                        text = "$choice. ${choiceText[index]}",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


@Composable
fun ScreenSetup(viewModel: MainViewModel) {
    val allCourses by viewModel.allCourses.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    MainScreen(
        allCourses = allCourses,
        searchResults = searchResults,
        viewModel = viewModel
    )
}

@Composable
fun MainScreen(
    allCourses: List<Course>,
    searchResults: List<Course>,
    viewModel: MainViewModel
) {
    var courseName by remember { mutableStateOf("") }
    var courseCreditHour by remember { mutableStateOf("") }
    var letterGrade by remember { mutableStateOf("") }

    var calculatedGPA by remember { mutableDoubleStateOf(-1.0) }
    var searching by remember { mutableStateOf(false) }

    val isGradeValid = letterGrade.isNotEmpty() &&
            letterGrade.uppercase() in validGrades

    val navyButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    )
    val orangeButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary
    )

    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        HeaderSection("Course GPA Tracker")

        CustomTextField(
            title = "Course Name",
            textState = courseName,
            onTextChange = { courseName = it },
            keyboardType = KeyboardType.Text
        )

        CustomTextField(
            title = "Credit Hour",
            textState = courseCreditHour,
            onTextChange = { courseCreditHour = it },
            keyboardType = KeyboardType.Number
        )

        CustomTextField(
            title = "Letter Grade",
            textState = letterGrade,
            onTextChange = { letterGrade = it.trim().uppercase() },
            keyboardType = KeyboardType.Text,
            isError = letterGrade.isNotEmpty() && !isGradeValid,
            errorMessage = "Valid grades: A, A-, B+, B, B-, C+, C, C-, D+, D, D-, F"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Button(
                    onClick = {
                        if (courseCreditHour.isNotEmpty() && isGradeValid) {
                            viewModel.insertCourse(
                                Course(
                                    courseName,
                                    courseCreditHour.toInt(),
                                    letterGrade
                                )
                            )
                            searching = false
                        }
                    },
                    colors = navyButtonColors,
                    enabled = isGradeValid
                ) { Text("Add") }

                Button(
                    onClick = {
                        searching = true
                        viewModel.findCourse(courseName)
                    },
                    colors = navyButtonColors
                ) { Text("Sch") }

                Button(
                    onClick = {
                        searching = false
                        viewModel.deleteCourse(courseName)
                    },
                    colors = navyButtonColors
                ) { Text("Del") }

                Button(
                    onClick = {
                        searching = false
                        courseName = ""
                        courseCreditHour = ""
                        letterGrade = ""
                    },
                    colors = navyButtonColors
                ) { Text("Clr") }

                Button(
                    onClick = {
                        calculatedGPA = calculateGPA2(allCourses)
                    },
                    colors = orangeButtonColors
                ) { Text("GPA") }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Current GPA", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (calculatedGPA < 0) "--" else "%.2f".format(calculatedGPA),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            val list = if (searching) searchResults else allCourses

            item {
                TitleRow("ID", "Course", "Credit", "Grade")
            }

            items(list) { course ->
                CourseRow(
                    id = course.id,
                    name = course.courseName,
                    creditHour = course.creditHour,
                    letterGrade = course.letterGrade
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(title: String) {
    Image(
        painter = painterResource(id = R.drawable.rwulogo),
        contentDescription = "Header Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }

    HorizontalDivider(
        color = MaterialTheme.colorScheme.tertiary,
        thickness = 3.dp
    )
}

// ===== GPA calculation =====
private fun calculateGPA2(courses: List<Course>): Double {
    val gradePoints = mapOf(
        "A" to 4.0, "A-" to 3.67,
        "B+" to 3.33, "B" to 3.0, "B-" to 2.67,
        "C+" to 2.33, "C" to 2.0, "C-" to 1.67,
        "D+" to 1.33, "D" to 1.0, "D-" to 0.67,
        "F" to 0.0
    )

    val totalCreditHours = courses.sumOf { it.creditHour }
    if (totalCreditHours == 0) return 0.0

    val totalPoints = courses.sumOf {
        it.creditHour * (gradePoints[it.letterGrade.uppercase()] ?: 0.0)
    }

    return totalPoints / totalCreditHours
}

@Composable
fun TitleRow(head1: String, head2: String, head3: String, head4: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 6.dp)
        ) {
            val color = MaterialTheme.colorScheme.onSecondary
            Text(head1, modifier = Modifier.weight(0.12f), color = color, fontWeight = FontWeight.Bold)
            Text(head2, modifier = Modifier.weight(0.38f), color = color, fontWeight = FontWeight.Bold)
            Text(head3, modifier = Modifier.weight(0.20f), color = color, fontWeight = FontWeight.Bold)
            Text(head4, modifier = Modifier.weight(0.20f), color = color, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.tertiary, thickness = 2.dp)
    }
}

@Composable
fun CourseRow(id: Int, name: String, creditHour: Int, letterGrade: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Text(id.toString(), modifier = Modifier.weight(0.12f))
            Text(name, modifier = Modifier.weight(0.38f))
            Text(creditHour.toString(), modifier = Modifier.weight(0.20f))
            Text(
                letterGrade,
                modifier = Modifier.weight(0.20f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        OutlinedTextField(
            value = textState,
            onValueChange = onTextChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            label = { Text(title) },
            isError = isError,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.secondary
            )
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

class MainViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(application) as T
    }
}
