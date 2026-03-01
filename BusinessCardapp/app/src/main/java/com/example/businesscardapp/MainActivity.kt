package com.example.businesscardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscardapp.ui.theme.BusinessCardAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCardAppTheme {
                val colors = listOf(Color(0xFFD2E8D4), Color(0xFFA3D9A5))
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(colors))
                    ) {
                        BusinessCardApp()
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessCardApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ){
            val image = painterResource(id = R.drawable.ic_android_logo)
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(12.dp)
            )
            Text(
                text = stringResource(id = R.string.full_name),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.role),
                color = Color(0xFF003E21),
                fontSize = 16.sp
            )
        }

        ContactInformation()
    }
}

@Composable
fun ContactInformation() {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        ContactRow(
            icon = Icons.Filled.Phone,
            text = stringResource(id = R.string.phone_number)
        )
        ContactRow(
            icon = Icons.Filled.Share,
            text = stringResource(id = R.string.social_handle)
        )
        ContactRow(
            icon = Icons.Filled.Email,
            text = stringResource(id = R.string.email_address)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        ContactRow(
            icon = Icons.Filled.Person,
            text = stringResource(id = R.string.linkedin_handle)
        )
        ContactRow(
            icon = Icons.Filled.PlayArrow,
            text = stringResource(id = R.string.instagram_handle)
        )
        ContactRow(
            icon = Icons.Filled.Info,
            text = stringResource(id = R.string.twitter_handle)
        )
        ContactRow(
            icon = Icons.Filled.Star,
            text = stringResource(id = R.string.reddit_handle)
        )
    }
}

@Composable
fun ContactRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF006D3B)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = text, color = Color.DarkGray)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BusinessCardPreview() {
    BusinessCardAppTheme {
        val colors = listOf(Color(0xFFD2E8D4), Color(0xFFA3D9A5))
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colors))
            ) {
                BusinessCardApp()
            }
        }
    }
}
