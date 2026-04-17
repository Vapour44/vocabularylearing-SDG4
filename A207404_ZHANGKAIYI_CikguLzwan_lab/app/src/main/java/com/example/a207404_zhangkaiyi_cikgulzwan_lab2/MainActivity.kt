package com.example.a207404_zhangkaiyi_cikgulzwan_lab2 // ⚠️ 确保包名正确！

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a207404_zhangkaiyi_cikgulzwan_lab2.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ⭐ 1. 使用你刚才在 Theme.kt 里弄好的 AppTheme
            AppTheme {
                // ⭐ 2. 加上 Surface，它会自动铺上你主题里的背景色
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleMainScreen()
                }
            }
        }
    }
}

// ==========================================
// 1. 颜色与字体配置 (模拟 Theme.kt 和 Color.kt)
// ==========================================
// ⭐ Lab 3 修改: 遵循规范，不再散落在各处，而是放入 colorScheme

val BrandFont = FontFamily.Default

// ==========================================
// 2. 主页面结构
// ==========================================
@Composable
fun SimpleMainScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable._026_chinese_gp_), // ⚠️ 注意图片名
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f)) // 稍微调高点透明度，黑夜模式才好看
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
        ) {
            TopSection()
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                )
            ) {
            BookSection()
            PlanSection()
            ButtonSection()
            FeatureSection()
        }
            AdSection()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White.copy(alpha = 0.95f))
        ) {
            BottomNav()
        }
    }
}

// ==========================================
// 3. 各个业务模块
// ==========================================

@Composable
fun TopSection() {
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
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            isSearchActive = false
                            searchText = ""
                            submittedText = ""
                        }
                )
            } else {
                Text("★ 40", fontSize = 18.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontFamily = BrandFont)
                Text("Rank | Task", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontFamily = BrandFont)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { isSearchActive = true }
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
                val studyAlpha = 1f
                Button(
                    onClick = { activeMode = if (activeMode == "Study") "None" else "Study" },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = studyAlpha)
                    )
                ) {
                    Text(
                        "Study",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BrandFont
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                val reviewAlpha = 1f
                Button(
                    onClick = { activeMode = if (activeMode == "Review") "None" else "Review" },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = reviewAlpha)
                    )
                ) {
                    Text(
                        "Review",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BrandFont
                    )
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

// ⭐ Lab 3 修改: Task 2 - 使用 ElevatedCard 替换 Row 的扁平背景
@Composable
fun BookSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        //elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(70.dp, 100.dp).background(MaterialTheme.colorScheme.primary)) {
                Text("IELTS\nCore", color = Color.White, modifier = Modifier.align(Alignment.Center), fontFamily = BrandFont)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("IELTS Core Vocab", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("Change >", fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))) {
                    Box(modifier = Modifier.fillMaxWidth(0.1f).height(8.dp).background(MaterialTheme.colorScheme.secondary))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("1 / 3272   328 Days Left", fontSize = 12.sp, color = Color.DarkGray)
            }
        }
    }
}

// ⭐ Lab 3 修改: Task 2 - 使用 Card 包装计划区块
@Composable
fun PlanSection() {

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Today's Plan", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Learned", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Text("0 / 10", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Reviewed", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Text("0 / 1", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

}

@Composable
fun FeatureSection() {
    var activeFeature by remember { mutableStateOf("None") }

    // ⭐ 新增：用一个 Card 把整个功能栏包裹起来，提供一个干净的底色

        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                FeatureItem("Reading", Icons.Default.Menu) { activeFeature = if (activeFeature == "Reading") "None" else "Reading" }
                FeatureItem("Training", Icons.Default.Build) { activeFeature = if (activeFeature == "Training") "None" else "Training" }
                FeatureItem("Wordbook", Icons.Default.List) { activeFeature = if (activeFeature == "Wordbook") "None" else "Wordbook" }
                FeatureItem("Self-Test", Icons.Default.Check) { activeFeature = if (activeFeature == "Self-Test") "None" else "Self-Test" }
            }

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
        modifier = Modifier

            .padding(horizontal = 6.dp, vertical = 8.dp), // 调整外边距
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ), // 85%透明度的白底

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onClick() }.padding(8.dp)
        ) {
            Image(
                imageVector = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = name,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AdSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        AdItem("IELTS 7+ Fast Track", "Learn only the essentials in 150h", "This course includes intensive reading and writing practices tailored for Band 7+.")
        Spacer(modifier = Modifier.height(16.dp))
        AdItem("Master 3000 Core Words", "30 mins a day to conquer vocab", "Utilize spaced repetition to permanently remember core vocabulary.")
        Spacer(modifier = Modifier.height(16.dp))
        AdItem("Master 90% of Scenarios", "Focus only on IELTS essentials", "Includes real listening and speaking scenarios from past exams.")
    }
}

// ⭐ Lab 3 修改: Task 3 - 动画展开的 Card
@Composable
fun AdItem(title: String, sub: String, details: String) {
    // 控制是否展开的状态
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded } // 点击切换状态
            .animateContentSize(),              // ⭐ 自动处理折叠/展开动画
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(sub, fontSize = 12.sp, color = Color.DarkGray)
                }
                Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.tertiary)) {
                    Image(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // 展开后显示额外内容
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = details, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun BottomNav() {
    var selectedItem by remember { mutableStateOf("Words") }
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem(name = "Words", icon = Icons.Default.Edit, isSelected = selectedItem == "Words") { selectedItem = "Words" }
        BottomNavItem(name = "Study", icon = Icons.Default.PlayArrow, isSelected = selectedItem == "Study") { selectedItem = "Study" }
        BottomNavItem(name = "Group", icon = Icons.Default.Face, isSelected = selectedItem == "Group") { selectedItem = "Group" }
        BottomNavItem(name = "Store", icon = Icons.Default.ShoppingCart, isSelected = selectedItem == "Store") { selectedItem = "Store" }
        BottomNavItem(name = "Me", icon = Icons.Default.Person, isSelected = selectedItem == "Me") { selectedItem = "Me" }
    }
}

@Composable
fun BottomNavItem(name: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
    val weight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(imageVector = icon, contentDescription = null, colorFilter = ColorFilter.tint(color))
        Text(text = name, fontSize = 10.sp, color = color, fontWeight = weight)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyScreen() {
    AppTheme {
        SimpleMainScreen()
    }
}