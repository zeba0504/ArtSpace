package com.example.artspace

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceApp()
                }
            }
        }
    }
}

data class Artwork(
    val imageResId: Int,
    val title: String,
    val description: String
)

@Composable
fun ArtSpaceApp() {
    var currentIndex by remember { mutableStateOf(0) }

    val artworks = listOf(
        Artwork(
            R.drawable.vang,
            "«Ночная терраса кафе»",
            "В своей уникальной манере глубокой перспективы и мягких цветов нидерланский живописец в 1877 году изобразил кафе в Арле (Франция)."
        ),
        Artwork(
            R.drawable.vang2,
            "«Звездная ночь»",
            "Сегодня это одна из самых узнаваемых картин. На ней изображены два совершенно разных состояния: безмятежность церковного шпиля и дикие краски ночного неба."
        )
    )

    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        LandscapeLayout(artworks, currentIndex) { newIndex ->
            currentIndex = newIndex
        }
    } else {
        PortraitLayout(artworks, currentIndex) { newIndex ->
            currentIndex = newIndex
        }
    }
}

@Composable
fun PortraitLayout(artworks: List<Artwork>, currentIndex: Int, onIndexChange: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Анимированный переход между карточками
        Crossfade(targetState = artworks[currentIndex], label = "ArtworkTransition") { artwork ->
            DisplayArtwork(artwork = artwork)
        }

        Spacer(modifier = Modifier.height(16.dp))

        NavigationButtons(
            artworks = artworks,
            currentIndex = currentIndex,
            onIndexChange = onIndexChange
        )
    }
}

@Composable
fun LandscapeLayout(artworks: List<Artwork>, currentIndex: Int, onIndexChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Crossfade(targetState = artworks[currentIndex], label = "ArtworkTransitionLandscape") { artwork ->
            DisplayArtwork(artwork = artwork, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.width(16.dp))

        NavigationButtons(
            artworks = artworks,
            currentIndex = currentIndex,
            onIndexChange = onIndexChange,
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Composable
fun DisplayArtwork(artwork: Artwork, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = artwork.imageResId),
                contentDescription = artwork.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(320.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = artwork.title,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = artwork.description,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun NavigationButtons(
    artworks: List<Artwork>,
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Button(
            onClick = {
                onIndexChange((currentIndex - 1 + artworks.size) % artworks.size)
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Previous")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = {
                onIndexChange((currentIndex + 1) % artworks.size)
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewArtSpaceApp() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}
