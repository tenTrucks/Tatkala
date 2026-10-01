package com.example.tatkala.ui.screens.settings.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF6D49AE)
private val DarkPurple = Color(0xFF4C258C)
private val Lime = Color(0xFFE7FCA7)
private val Background = Color(0xFFF8F8F8)

/*
 * Data sementara untuk Profile.
 * Tidak menggunakan database atau model baru.
 */
object ProfileEditState {

    var fullName by mutableStateOf("Sutan")

    var username by mutableStateOf("@sutan")

    var email by mutableStateOf("sutan@example.com")

    var phoneNumber by mutableStateOf("081234567890")

    var profileImageUri by mutableStateOf<Uri?>(null)
}

@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {

    // Data yang sedang diedit
    var fullName by remember {
        mutableStateOf(ProfileEditState.fullName)
    }

    var username by remember {
        mutableStateOf(
            ProfileEditState.username.removePrefix("@")
        )
    }

    var email by remember {
        mutableStateOf(ProfileEditState.email)
    }

    var phoneNumber by remember {
        mutableStateOf(ProfileEditState.phoneNumber)
    }

    var selectedImage by remember {
        mutableStateOf<Bitmap?>(null)
    }

    // =========================
    // GALERI
    // =========================
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        if (uri != null) {

            ProfileEditState.profileImageUri = uri

            // Ambil gambar dari URI
            try {
                val inputStream =
                    androidx.compose.ui.platform.LocalContext.current
                        .contentResolver
                        .openInputStream(uri)

                selectedImage = BitmapFactory.decodeStream(inputStream)

                inputStream?.close()

            } catch (_: Exception) {
                selectedImage = null
            }
        }
    }

    val context = androidx.compose.ui.platform.LocalContext.current

    // Load foto yang sebelumnya sudah dipilih
    LaunchedEffect(ProfileEditState.profileImageUri) {

        val uri = ProfileEditState.profileImageUri

        if (uri != null) {

            try {
                val inputStream =
                    context.contentResolver.openInputStream(uri)

                selectedImage =
                    BitmapFactory.decodeStream(inputStream)

                inputStream?.close()

            } catch (_: Exception) {
                selectedImage = null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // =========================
        // HEADER
        // =========================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Edit Profile",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkPurple
            )
        }

        // =========================
        // CONTENT
        // =========================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =========================
            // FOTO PROFIL
            // =========================
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Lime)
                    .clickable {
                        galleryLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {

                if (selectedImage != null) {

                    Image(
                        bitmap = selectedImage!!.asImageBitmap(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(65.dp),
                        tint = Purple
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Tap to change photo",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =========================
            // FULL NAME
            // =========================
            ProfileTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                },
                label = "Full Name"
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =========================
            // USERNAME
            // =========================
            ProfileTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = "Username"
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =========================
            // EMAIL
            // =========================
            ProfileTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = "Email"
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =========================
            // NO. TELEPON
            // =========================
            ProfileTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                },
                label = "No. Telepon"
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =========================
            // SAVE CHANGES
            // =========================
            Button(
                onClick = {

                    // Simpan semua perubahan
                    ProfileEditState.fullName = fullName

                    ProfileEditState.username =
                        "@$username"

                    ProfileEditState.email = email

                    ProfileEditState.phoneNumber =
                        phoneNumber

                    // Kembali ke Profile
                    onSave()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple
                )
            ) {

                Text(
                    text = "Save Changes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(text = label)
        },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Purple,
            focusedLabelColor = Purple,
            cursorColor = Purple
        ),
        singleLine = true
    )
}