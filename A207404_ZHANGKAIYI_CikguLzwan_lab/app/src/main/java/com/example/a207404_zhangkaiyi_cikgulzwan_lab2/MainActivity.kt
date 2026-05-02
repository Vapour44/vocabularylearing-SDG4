package com.example.a207404_zhangkaiyi_cikgulzwan_lab2

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a207404_zhangkaiyi_cikgulzwan_lab2.ui.theme.AppTheme

// ==========================================
// ⭐ Data Classes
// ==========================================
data class UserProfile(
    val name: String = "",
    val targetScore: String = ""
)

data class WordBookItem(
    val title: String,
    val count: Int
)

// ==========================================
// ⭐ ViewModel (Shared State & ADD logic)
// ==========================================
class UserViewModel : ViewModel() {
    var userProfile by mutableStateOf(UserProfile())
        private set

    var wordBooks by mutableStateOf(listOf(WordBookItem("Favorite Words", 0)))
        private set

    fun updateProfile(name: String, targetScore: String) {
        userProfile = userProfile.copy(name = name, targetScore = targetScore)
    }

    fun addWordBook(title: String) {
        val newBook = WordBookItem(title = title, count = 0)
        wordBooks = wordBooks + newBook
    }

    // ⭐ NEW: Function to delete a wordbook
    fun removeWordBook(book: WordBookItem) {
        wordBooks = wordBooks - book
    }
}

val BrandFont = FontFamily.Default

// ==========================================
// ⭐ Main Activity
// ==========================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IeltsAppNavigation()
                }
            }
        }
    }
}

// ==========================================
// ⭐ Navigation Control (7 Screens)
// ==========================================
@Composable
fun IeltsAppNavigation() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel() // Single instance

    NavHost(navController = navController, startDestination = "profile_setup") {
        composable("profile_setup") { ProfileSetupScreen(navController, userViewModel) }
        composable("main_screen") { SimpleMainScreen(navController, userViewModel) }
        composable("course_details/{courseTitle}") { backStackEntry ->
            val courseTitle = backStackEntry.arguments?.getString("courseTitle") ?: "Course"
            CourseDetailScreen(navController, courseTitle)
        }
        composable("reading_screen") { ReadingScreen(navController) }
        composable("training_screen") { TrainingScreen(navController) }
        composable("wordbook_screen") { WordbookScreen(navController, userViewModel) }
        composable("selftest_screen") { SelfTestScreen(navController) }
    }
}

// ==========================================
// Screen 1: Profile Setup Form
// ==========================================
@Composable
fun ProfileSetupScreen(navController: NavController, viewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    var targetScore by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Welcome to IELTS Master", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(32.dp))

        TextField(value = name, onValueChange = { name = it }, label = { Text("Your Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = targetScore, onValueChange = { targetScore = it }, label = { Text("Target Band Score (e.g. 7.5)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.updateProfile(name, targetScore)
                navController.navigate("main_screen") { popUpTo("profile_setup") { inclusive = true } }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Start Learning", fontSize = 18.sp)
        }
    }
}

// ==========================================
// Screen 2: Main Screen
// ==========================================
@Composable
fun SimpleMainScreen(navController: NavController, viewModel: UserViewModel) {
    val userProfile = viewModel.userProfile

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable._026_chinese_gp_), // Keep your original background image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
        ) {
            TopSection(userName = userProfile.name, targetScore = userProfile.targetScore, wordBookCount = viewModel.wordBooks.size)

            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                )
            ) {
                BookSection()
                PlanSection()
                ButtonSection()
                FeatureSection(navController)
            }
            AdSection(navController)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
        ) {
            BottomNav()
        }
    }
}

@Composable
fun TopSection(userName: String, targetScore: String, wordBookCount: Int) {
    var isSearchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var submittedText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSearchActive) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search words...", fontSize = 14.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            submittedText = searchText
                            focusManager.clearFocus()
                            isSearchActive = false
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.size(28.dp).clickable {
                        isSearchActive = false
                        searchText = ""
                        submittedText = ""
                    }
                )
            } else {
                Column {
                    Text("Hi, $userName", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text("Target: $targetScore | Books: $wordBookCount", fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.size(28.dp).clickable { isSearchActive = true }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        if (submittedText.isNotEmpty() && !isSearchActive) {
            Text(
                text = "▶ Searched for: $submittedText",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontFamily = BrandFont,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ButtonSection() {
    var activeMode by remember { mutableStateOf("None") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { activeMode = if (activeMode == "Study") "None" else "Study" },
                modifier = Modifier.weight(1f).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Study", color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = BrandFont)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { activeMode = if (activeMode == "Review") "None" else "Review" },
                modifier = Modifier.weight(1f).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Review", color = MaterialTheme.colorScheme.onSecondary, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = BrandFont)
            }
        }

        if (activeMode != "None") {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "▶ Mode: $activeMode Active! welcome to $activeMode",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun FeatureSection(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            FeatureItem("Reading", Icons.Default.Menu) { navController.navigate("reading_screen") }
            FeatureItem("Training", Icons.Default.Build) { navController.navigate("training_screen") }
            FeatureItem("Wordbook", Icons.Default.List) { navController.navigate("wordbook_screen") }
            FeatureItem("Self-Test", Icons.Default.Check) { navController.navigate("selftest_screen") }
        }
    }
}

@Composable
fun FeatureItem(name: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onClick() }.padding(2.dp)
        ) {
            Image(imageVector = icon, contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary), modifier = Modifier.size(30.dp))
            Text(text = name, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AdSection(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        AdItem("IELTS 7+ Fast Track", "Learn only the essentials in 150h", "This course includes intensive reading and writing practices tailored for Band 7+.", navController)
        Spacer(modifier = Modifier.height(16.dp))
        AdItem("Master 3000 Core Words", "30 mins a day to conquer vocab", "Utilize spaced repetition to permanently remember core vocabulary.", navController)
        Spacer(modifier = Modifier.height(16.dp))
        AdItem("Master 90% of Scenarios", "Focus only on IELTS essentials", "Includes real listening and speaking scenarios from past exams.", navController)
    }
}

@Composable
fun AdItem(title: String, sub: String, details: String, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(sub, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.tertiary)) {
                    Image(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = details, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        val safeTitle = Uri.encode(title)
                        navController.navigate("course_details/$safeTitle")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Course Details")
                }
            }
        }
    }
}

// ==========================================
// Screen 3: Course Detail Screen
// ==========================================
@Composable
fun CourseDetailScreen(navController: NavController, courseTitle: String) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp).statusBarsPadding()
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp).clickable { navController.popBackStack() }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Course Details", fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
        Text(courseTitle, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("About this course", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text("This is the detailed syllabus for $courseTitle. You will learn advanced techniques to improve your score rapidly.", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun BookSection() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(70.dp, 100.dp).background(MaterialTheme.colorScheme.primary)) {
                Text("IELTS\nCore", color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.align(Alignment.Center), fontFamily = BrandFont)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("IELTS Core Vocab", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("Change >", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))) {
                    Box(modifier = Modifier.fillMaxWidth(0.1f).height(8.dp).background(MaterialTheme.colorScheme.secondary))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("1 / 3272   328 Days Left", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun PlanSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Today's Plan", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Learned", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("0 / 10", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Reviewed", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("0 / 1", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun BottomNav() {
    var selectedItem by remember { mutableStateOf("Words") }
    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceAround) {
        BottomNavItem("Words", Icons.Default.Edit, selectedItem == "Words") { selectedItem = "Words" }
        BottomNavItem("Study", Icons.Default.PlayArrow, selectedItem == "Study") { selectedItem = "Study" }
        BottomNavItem("Group", Icons.Default.Face, selectedItem == "Group") { selectedItem = "Group" }
        BottomNavItem("Store", Icons.Default.ShoppingCart, selectedItem == "Store") { selectedItem = "Store" }
        BottomNavItem("Me", Icons.Default.Person, selectedItem == "Me") { selectedItem = "Me" }
    }
}

@Composable
fun BottomNavItem(name: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    val weight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Image(imageVector = icon, contentDescription = null, colorFilter = ColorFilter.tint(color))
        Text(text = name, fontSize = 10.sp, color = color, fontWeight = weight)
    }
}

// ==========================================
// 🚀 Screen 4: Reading Screen (Theme Applied)
// ==========================================
@Composable
fun ReadingScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        // Back Button Header
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("Reading Hub", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        // Top Alert Bar
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondary).padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("🔺 Course Completed", color = MaterialTheme.colorScheme.onSecondary, fontWeight = FontWeight.Bold)
            Text("View Report >", color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.8f))
        }

        // ⭐ Horizontal Scrollable Book Display (Now with spaced items)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .horizontalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp), // Adds space between books
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book 1
            ReadingBookCard("FROZEN", "Disney Classic", 0.8f, "80%", Color(0xFF74C6DA))

            // ⭐ Book 2 (New book added to enable scrolling)
            ReadingBookCard("ZOOPHOBIA", "Disney Modern", 0.3f, "30%", Color(0xFFFFB74D))
        }

        // Bottom Tabs & Navigation

            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceAround) {
                BottomNavItem("For You", Icons.Default.Home, false) { navController.popBackStack() }
                BottomNavItem("Reading", Icons.Default.Menu, true) { }
                BottomNavItem("Profile", Icons.Default.Person, false) { }
            }

    }
}

// ⭐ NEW: Reusable Book Card Component to keep code clean
@Composable
fun ReadingBookCard(title: String, subtitle: String, progress: Float, progressText: String, coverColor: Color) {
    Card(
        modifier = Modifier.width(220.dp).height(380.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(coverColor)) {
                Text(title, modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(subtitle, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Read ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LinearProgressIndicator(progress = progress, modifier = Modifier.weight(1f).height(4.dp), color = MaterialTheme.colorScheme.primary)
                Text(" $progressText", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Action */ },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Start Chap. 1", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

// ==========================================
// 🚀 Screen 5: Training Screen (Theme & Alignment Fixed)
// ==========================================
@Composable
fun TrainingScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        // ⭐ Back Button Header
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text("Word Training", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {
            Text("Current Plan: IELTS Core   |   Words Learned: 1", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            // Battle Banner
            Card(modifier = Modifier.fillMaxWidth().height(80.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("1V1 Word Battle", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondary, fontSize = 20.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Text("Challenge >", color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Quick Training Section
            Text("Quick Training", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                TrainingCard(title = "Self Check", subtitle = "", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                TrainingCard(title = "Quick Listen", subtitle = "", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Specialized Training Section
            Text("Specialized Training + Credits", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                TrainingCard("Eng to Def", "Reading", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                TrainingCard("Def to Eng", "Reading", Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                TrainingCard("Spelling", "Writing", Modifier.weight(1f)) // Removed "isNew"
                Spacer(modifier = Modifier.width(16.dp))
                TrainingCard("Listen & Choose", "Listening", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Expansion Section
            Text("Vocabulary Expansion", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.width(160.dp).height(80.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Word Videos", color = MaterialTheme.colorScheme.onTertiary, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// Training Card Alignment Fixed
@Composable
fun TrainingCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier.height(75.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Using weight here forces the title column to take up remaining space, pushing the "0/0" box properly to the right edge.
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                if (subtitle.isNotEmpty()) {
                    Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text("0/0", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// 🚀 Screen 6: Wordbook Screen (Theme Applied)
// ==========================================
@Composable
fun WordbookScreen(navController: NavController, viewModel: UserViewModel) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newBookName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        // Back Button Header
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text("Wordbooks (${viewModel.wordBooks.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.weight(1f))
        }

        LazyColumn(modifier = Modifier.weight(1f).padding(16.dp)) {
            items(viewModel.wordBooks) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    // ⭐ Modified Row to accommodate the Delete button
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween // Pushes delete button to the right
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(60.dp, 80.dp).background(MaterialTheme.colorScheme.tertiary)) {
                                Text("Word\nBook", color = MaterialTheme.colorScheme.onTertiary, modifier = Modifier.align(Alignment.Center), fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(book.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Total: ${book.count} Words", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        // ⭐ NEW: Delete Button
                        IconButton(onClick = { viewModel.removeWordBook(book) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Book",
                                tint = MaterialTheme.colorScheme.error // Uses standard red/error color
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("+ New Wordbook", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Create New Wordbook") },
            text = {
                TextField(
                    value = newBookName,
                    onValueChange = { newBookName = it },
                    placeholder = { Text("Enter wordbook name") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newBookName.isNotBlank()) {
                        viewModel.addWordBook(newBookName)
                        newBookName = ""
                        showAddDialog = false
                    }
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// ==========================================
// 🚀 Screen 7: Self-Test Screen (Theme Applied)
// ==========================================
@Composable
fun SelfTestScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        // ⭐ Back Button Header
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("To Review: 1", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("review word", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        Row(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { /* Logic */ },
                modifier = Modifier.weight(1f).height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text("Don't Remember", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { /* Logic */ },
                modifier = Modifier.weight(1f).height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text("Remember", color = MaterialTheme.colorScheme.onPrimary, fontSize = 15.sp)
            }
        }
    }
}