package com.dariusepure.wrxpedia.ui.compare

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dariusepure.wrxpedia.ui.components.GenerationImageView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    viewModel: CompareViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Side-by-Side Comparison",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Selector Header Controls
                CompareSelectorRow(
                    options = state.availableOptions,
                    option1 = state.option1,
                    option2 = state.option2,
                    onSelectOption1 = viewModel::selectOption1,
                    onSelectOption2 = viewModel::selectOption2,
                    onSwap = viewModel::swapOptions
                )

                val opt1 = state.option1
                val opt2 = state.option2

                if (opt1 != null && opt2 != null) {
                    // Power & Torque Summary Header
                    PowerTorqueSummaryCard(opt1 = opt1, opt2 = opt2)

                    // Specs Comparison Table
                    CompareSpecSection(title = "Engine & Turbo") {
                        CompareMetricRow(
                            label = "Engine Code",
                            val1 = opt1.spec.engineCode,
                            val2 = opt2.spec.engineCode
                        )
                        CompareMetricRow(
                            label = "Horsepower",
                            val1 = "${opt1.spec.powerHp} HP",
                            val2 = "${opt2.spec.powerHp} HP",
                            highlightWinner = true,
                            isVal1Better = opt1.spec.powerHp > opt2.spec.powerHp,
                            isVal2Better = opt2.spec.powerHp > opt1.spec.powerHp
                        )
                        CompareMetricRow(
                            label = "Torque Output",
                            val1 = "${opt1.spec.torqueNm} Nm",
                            val2 = "${opt2.spec.torqueNm} Nm",
                            highlightWinner = true,
                            isVal1Better = opt1.spec.torqueNm > opt2.spec.torqueNm,
                            isVal2Better = opt2.spec.torqueNm > opt1.spec.torqueNm
                        )
                        CompareMetricRow(
                            label = "Turbocharger",
                            val1 = opt1.spec.turboModel,
                            val2 = opt2.spec.turboModel
                        )
                        CompareMetricRow(
                            label = "Stock Boost",
                            val1 = opt1.spec.stockBoost,
                            val2 = opt2.spec.stockBoost
                        )
                    }

                    CompareSpecSection(title = "Transmission & Drivetrain") {
                        CompareMetricRow(
                            label = "Transmission",
                            val1 = opt1.spec.transmission,
                            val2 = opt2.spec.transmission
                        )
                        CompareMetricRow(
                            label = "Drive System",
                            val1 = opt1.spec.driveType,
                            val2 = opt2.spec.driveType
                        )
                        CompareMetricRow(
                            label = "Final Drive",
                            val1 = opt1.spec.finalDrive,
                            val2 = opt2.spec.finalDrive
                        )
                    }

                    CompareSpecSection(title = "Chassis & Brakes") {
                        CompareMetricRow(
                            label = "Chassis Code",
                            val1 = opt1.spec.chassisCode,
                            val2 = opt2.spec.chassisCode
                        )
                        CompareMetricRow(
                            label = "Bolt Pattern (PCD)",
                            val1 = opt1.spec.pcd,
                            val2 = opt2.spec.pcd
                        )
                        CompareMetricRow(
                            label = "Brake System",
                            val1 = opt1.spec.brakes,
                            val2 = opt2.spec.brakes
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompareSelectorRow(
    options: List<CompareOption>,
    option1: CompareOption?,
    option2: CompareOption?,
    onSelectOption1: (CompareOption) -> Unit,
    onSelectOption2: (CompareOption) -> Unit,
    onSwap: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CompareDropdownButton(
                currentOption = option1,
                options = options,
                onSelect = onSelectOption1
            )
        }

        IconButton(
            onClick = onSwap,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                contentDescription = "Swap",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            CompareDropdownButton(
                currentOption = option2,
                options = options,
                onSelect = onSelectOption2
            )
        }
    }
}

@Composable
fun CompareDropdownButton(
    currentOption: CompareOption?,
    options: List<CompareOption>,
    onSelect: (CompareOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        onClick = { expanded = true },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentOption != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    GenerationImageView(
                        iconName = currentOption.iconName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentOption.generationName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currentOption.variantTitle,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Text(
                    text = "Select Model",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = opt.generationName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = opt.variantTitle,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    onClick = {
                        onSelect(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PowerTorqueSummaryCard(
    opt1: CompareOption,
    opt2: CompareOption
) {
    val hpDiff = opt2.spec.powerHp - opt1.spec.powerHp
    val torqueDiff = opt2.spec.torqueNm - opt1.spec.torqueNm

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "TELEMETRY SUMMARY",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Model 1 Summary
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = opt1.variantTitle,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${opt1.spec.powerHp} HP",
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${opt1.spec.torqueNm} Nm",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "VS",
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                // Model 2 Summary
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = opt2.variantTitle,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${opt2.spec.powerHp} HP",
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${opt2.spec.torqueNm} Nm",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val hpText = if (hpDiff > 0) "+$hpDiff HP" else if (hpDiff < 0) "$hpDiff HP" else "Same HP"
            val torqueText = if (torqueDiff > 0) "+$torqueDiff Nm" else if (torqueDiff < 0) "$torqueDiff Nm" else "Same Torque"

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Delta: $hpText • $torqueText",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun CompareSpecSection(
    title: String,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            content()
        }
    }
}

@Composable
fun CompareMetricRow(
    label: String,
    val1: String,
    val2: String,
    highlightWinner: Boolean = false,
    isVal1Better: Boolean = false,
    isVal2Better: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (highlightWinner && isVal1Better) Color(0xFF16A34A).copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = val1,
                    fontWeight = if (highlightWinner && isVal1Better) FontWeight.ExtraBold else FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (highlightWinner && isVal1Better) Color(0xFF22C55E) else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (highlightWinner && isVal2Better) Color(0xFF16A34A).copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = val2,
                    fontWeight = if (highlightWinner && isVal2Better) FontWeight.ExtraBold else FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (highlightWinner && isVal2Better) Color(0xFF22C55E) else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}
