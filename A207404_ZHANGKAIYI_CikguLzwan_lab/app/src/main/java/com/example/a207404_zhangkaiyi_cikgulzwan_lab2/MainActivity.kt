package com.example.a207404_zhangkaiyi_cikgulzwan_lab2 // ⚠️ 确保包名正确！

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
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
// ⭐ Lab 4: Data Class
// ==========================================
data class UserProfile(
    val name: String = "",
    val targetScore: String = ""

)

// ==========================================
// ⭐ Lab 4: ViewModel
// ==========================================
class UserViewModel : ViewModel() {
    var userProfile by mutableStateOf(UserProfile())
        private set

    fun updateProfile(name: String, targetScore: String) {
        userProfile = UserProfile(name, targetScore)
    }


}

val BrandFont = FontFamily.Default

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IeltsAppNavigation() // 启动导航
                }
            }
        }
    }
}

// ==========================================
// ⭐ Lab 4: Navigation Control (三个页面)
// ==========================================
@Composable
fun IeltsAppNavigation() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = "profile_setup") {
        // Screen 1: 表单页
        composable("profile_setup") { ProfileSetupScreen(navController, userViewModel) }
        // Screen 2: 主页 (你原本的 Lab 3)
        composable("main_screen") { SimpleMainScreen(navController, userViewModel) }
        // Screen 3: 详情页
        composable("course_details/{courseTitle}") { backStackEntry ->
            val courseTitle = backStackEntry.arguments?.getString("courseTitle") ?: "Course"
            CourseDetailScreen(navController, courseTitle)
        }
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
        Text("Welcome to IELTS Master", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
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
// Screen 2: Main Screen (完整保留你的 Lab3 逻辑)
// ==========================================
@Composable
fun SimpleMainScreen(navController: NavController, viewModel: UserViewModel) {
    val userProfile = viewModel.userProfile

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable._026_chinese_gp_), // ⚠️ 注意图片名
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.4f)) // 适配黑夜模式
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
        ) {
            // ⭐ 传入 ViewModel 的数据
            TopSection(userName = userProfile.name, targetScore = userProfile.targetScore)

            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f) // 适配黑夜模式
                )
            ) {
                BookSection()
                PlanSection()
                ButtonSection() // 完整保留的 Button
                FeatureSection() // 完整保留的 Feature
            }
            AdSection(navController) // 传入 navController 方便点击跳转
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

// ⭐ 完整保留搜索交互，同时整合 ViewModel 数据
@Composable
fun TopSection(userName: String, targetScore: String) {
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
                    Text("Target Band: $targetScore", fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
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

        // ⭐ 原封不动保留的搜索结果显示逻辑
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

// ⭐ 原封不动保留 activeMode 状态提示
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

        // ⭐ 你的原本逻辑
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

// ⭐ 原封不动保留 activeFeature 状态提示
@Composable
fun FeatureSection() {
    var activeFeature by remember { mutableStateOf("None") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            FeatureItem("Reading", Icons.Default.Menu) { activeFeature = if (activeFeature == "Reading") "None" else "Reading" }
            FeatureItem("Training", Icons.Default.Build) { activeFeature = if (activeFeature == "Training") "None" else "Training" }
            FeatureItem("Wordbook", Icons.Default.List) { activeFeature = if (activeFeature == "Wordbook") "None" else "Wordbook" }
            FeatureItem("Self-Test", Icons.Default.Check) { activeFeature = if (activeFeature == "Self-Test") "None" else "Self-Test" }
        }

        // ⭐ 你的原本逻辑
        if (activeFeature != "None") {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "▶ : $activeFeature",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
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
            modifier = Modifier.clickable { onClick() }.padding(8.dp)
        ) {
            Image(imageVector = icon, contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary), modifier = Modifier.size(32.dp))
            Text(text = name, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AdSection(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        // ⭐ 保留三个广告栏
        AdItem("IELTS 7+ Fast Track", "Learn only the essentials in 150h", "This course includes intensive reading and writing practices tailored for Band 7+.", navController)
        Spacer(modifier = Modifier.height(16.dp))
        AdItem("Master 3000 Core Words", "30 mins a day to conquer vocab", "Utilize spaced repetition to permanently remember core vocabulary.", navController)
        Spacer(modifier = Modifier.height(16.dp))
        AdItem("Master 90% of Scenarios", "Focus only on IELTS essentials", "Includes real listening and speaking scenarios from past exams.", navController)
    }
}

// ⭐ 保留展开动画，只在展开后加了一个跳转按钮
@Composable
fun AdItem(title: String, sub: String, details: String, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(), // ⭐ 保留你原有的动画
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
                // ⭐ 新增：满足 Lab4 要求的跳转按钮
                Button(
                    onClick = { val safeTitle = Uri.encode(title);navController.navigate("course_details/$safeTitle") },
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

// ==========================================
// 其他无需修改的 UI 组件 (BookSection, PlanSection, BottomNav)
// ==========================================
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