package com.dariusepure.wrxpedia.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dariusepure.wrxpedia.data.model.CarSpec
import com.dariusepure.wrxpedia.data.model.SpecialEdition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerationDetailScreen(
    viewModel: DetailViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val generation = state.generation

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = generation?.name ?: "Generation Details",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        generation?.nickname?.let { nickname ->
                            Text(
                                text = nickname + " (" + generation.years + ")",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
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
        if (state.isLoading || generation == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // TabRow [ WRX ] | [ STI ] | [ Special Editions ]
                val availableTabs = mutableListOf<VariantTab>()
                if (generation.variants.wrx != null) availableTabs.add(VariantTab.WRX)
                if (generation.variants.sti != null) availableTabs.add(VariantTab.STI)
                if (!generation.variants.specialEditions.isNullOrEmpty()) availableTabs.add(VariantTab.SPECIAL_EDITIONS)

                val selectedTabIndex = availableTabs.indexOf(state.selectedTab).coerceAtLeast(0)

                if (availableTabs.isNotEmpty()) {
                    PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                        availableTabs.forEach { tab ->
                            val title = when (tab) {
                                VariantTab.WRX -> "WRX"
                                VariantTab.STI -> "STI"
                                VariantTab.SPECIAL_EDITIONS -> "Special Editions"
                            }
                            Tab(
                                selected = state.selectedTab == tab,
                                onClick = { viewModel.selectTab(tab) },
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (state.selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (state.selectedTab) {
                        VariantTab.WRX -> {
                            generation.variants.wrx?.let { spec ->
                                CarSpecAccordionView(
                                    spec = spec,
                                    expandedSections = state.expandedSections,
                                    onToggleSection = viewModel::toggleSection
                                )
                            }
                        }
                        VariantTab.STI -> {
                            generation.variants.sti?.let { spec ->
                                CarSpecAccordionView(
                                    spec = spec,
                                    expandedSections = state.expandedSections,
                                    onToggleSection = viewModel::toggleSection
                                )
                            }
                        }
                        VariantTab.SPECIAL_EDITIONS -> {
                            generation.variants.specialEditions?.forEach { edition ->
                                SpecialEditionCard(edition = edition)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarSpecAccordionView(
    spec: CarSpec,
    expandedSections: Set<DetailSection>,
    onToggleSection: (DetailSection) -> Unit
) {
    // Section 1: Engine & Performance
    AccordionCard(
        title = "1. Engine & Performance",
        icon = Icons.Default.Speed,
        isExpanded = expandedSections.contains(DetailSection.ENGINE_PERFORMANCE),
        onToggle = { onToggleSection(DetailSection.ENGINE_PERFORMANCE) }
    ) {
        DetailRow(label = "Engine Code", value = spec.engineCode)
        DetailRow(label = "Max Power", value = "${spec.powerHp} HP")
        DetailRow(label = "Max Torque", value = "${spec.torqueNm} Nm")
        DetailRow(label = "Turbocharger Model", value = spec.turboModel)
        DetailRow(label = "Stock Boost Pressure", value = spec.stockBoost)
        DetailRow(label = "Intercooler Type", value = spec.intercooler)
    }

    // Section 2: Transmission & Drivetrain
    AccordionCard(
        title = "2. Transmission & Drivetrain",
        icon = Icons.Default.Build,
        isExpanded = expandedSections.contains(DetailSection.TRANSMISSION_DRIVETRAIN),
        onToggle = { onToggleSection(DetailSection.TRANSMISSION_DRIVETRAIN) }
    ) {
        DetailRow(label = "Transmission", value = spec.transmission)
        DetailRow(label = "Drive System", value = spec.driveType)
        DetailRow(label = "Final Drive Ratio", value = spec.finalDrive)
    }

    // Section 3: Maintenance & Fluids
    AccordionCard(
        title = "3. Maintenance & Fluids",
        icon = Icons.Default.Opacity,
        isExpanded = expandedSections.contains(DetailSection.MAINTENANCE_FLUIDS),
        onToggle = { onToggleSection(DetailSection.MAINTENANCE_FLUIDS) }
    ) {
        DetailRow(label = "Engine Oil Capacity", value = spec.engineOilCapacity)
        DetailRow(label = "Engine Oil Viscosity", value = spec.engineOilViscosity)
        DetailRow(label = "Gearbox Oil Capacity", value = spec.gearboxOilCapacity)
        DetailRow(label = "Rear Diff Oil Capacity", value = spec.diffOilCapacity)
        DetailRow(label = "Recommended Spark Plugs", value = spec.sparkPlugs)
        DetailRow(label = "Coolant Capacity", value = spec.coolantCapacity)
    }

    // Section 4: Chassis, Brakes & Dimensions
    AccordionCard(
        title = "4. Chassis, Brakes & Dimensions",
        icon = Icons.Default.DirectionsCar,
        isExpanded = expandedSections.contains(DetailSection.CHASSIS_BRAKES_DIMENSIONS),
        onToggle = { onToggleSection(DetailSection.CHASSIS_BRAKES_DIMENSIONS) }
    ) {
        DetailRow(label = "Chassis Code", value = spec.chassisCode)
        DetailRow(label = "Bolt Pattern (PCD)", value = spec.pcd)
        DetailRow(label = "Brake System", value = spec.brakes)
    }

    // Section 5: Buyer's Guide & Known Issues
    AccordionCard(
        title = "5. Buyer's Guide & Known Issues",
        icon = Icons.Default.Warning,
        isExpanded = expandedSections.contains(DetailSection.BUYERS_GUIDE_KNOWN_ISSUES),
        onToggle = { onToggleSection(DetailSection.BUYERS_GUIDE_KNOWN_ISSUES) }
    ) {
        Text(
            text = "Specific Issues & Inspection Points:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        spec.knownIssues.forEach { issue ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "• ",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = issue,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun AccordionCard(
    title: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    HorizontalDivider(
                        color = DividerDefaults.color.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    content()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SpecialEditionCard(edition: SpecialEdition) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = edition.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Text(
                        text = edition.year,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Production: ${edition.unitsProduced} • Engine: ${edition.engineCode} (${edition.powerHp} HP)",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = edition.description,
                style = MaterialTheme.typography.bodyMedium
            )

            if (edition.keyFeatures.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Key Features:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
                edition.keyFeatures.forEach { feature ->
                    Text(
                        text = "• $feature",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}
