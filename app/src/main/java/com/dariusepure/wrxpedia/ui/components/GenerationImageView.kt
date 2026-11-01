package com.dariusepure.wrxpedia.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@SuppressLint("DiscouragedApi")
@Composable
fun GenerationImageView(
    iconName: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    val normalizedIcon = iconName.lowercase()
    val packageName = context.packageName
    val resources = context.resources

    val possibleNames = mutableListOf(
        "wrx_$normalizedIcon",
        normalizedIcon,
    )

    if ((normalizedIcon == "classic") || (normalizedIcon == "meaneye")) {
        possibleNames.add("wrx_meaneye")
        possibleNames.add("meaneye")
        possibleNames.add("wrx_classic")
        possibleNames.add("classic")
    }
    if ((normalizedIcon == "va") || (normalizedIcon == "raptoreye")) {
        possibleNames.add("wrx_raptoreye")
        possibleNames.add("wrx_va")
        possibleNames.add("raptoreye")
        possibleNames.add("va")
    }
    if ((normalizedIcon == "hatchback") || (normalizedIcon == "sedan") || (normalizedIcon == "stinkeye")) {
        possibleNames.add("wrx_stinkeye")
        possibleNames.add("stinkeye")
        possibleNames.add("wrx_hatchback")
        possibleNames.add("wrx_sedan")
    }

    var resId = 0
    for (name in possibleNames) {
        resId = resources.getIdentifier(name, "drawable", packageName)
        if (resId != 0) {
            break
        }
    }

    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale,
        )
    } else {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
