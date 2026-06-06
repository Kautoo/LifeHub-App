package com.fizi.lifehub.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fizi.lifehub.ui.theme.*

// ─── Glass Card ───
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(cornerRadius),
            ambientColor = Primary.copy(alpha = 0.08f),
            spotColor = Primary.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    ) {
        Column(content = content)
    }
}

// ─── Stat Pill ───
@Composable
fun StatPill(
    label: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(icon, fontSize = 14.sp)
            Text(
                value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Section Header ───
@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    action: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        action?.invoke()
    }
}

// ─── Emoji Circle ───
@Composable
fun EmojiCircle(
    emoji: String,
    size: Dp = 48.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = (size.value * 0.5).sp)
    }
}

// ─── Gradient FAB ───
@Composable
fun GradientFAB(
    onClick: () -> Unit,
    icon: ImageVector,
    gradient: Brush = GradientPrimary,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.shadow(
            elevation = 12.dp,
            shape = CircleShape,
            ambientColor = Primary.copy(alpha = 0.3f),
            spotColor = Primary.copy(alpha = 0.3f)
        ),
        shape = CircleShape,
        containerColor = Color.Transparent,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(gradient, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        }
    }
}

// ─── Rich Empty State ───
@Composable
fun EmptyState(
    emoji: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated floating emoji
        val infiniteTransition = rememberInfiniteTransition(label = "float")
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -12f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            ),
            label = "offsetY"
        )
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(y = offsetY.dp)
                .shadow(16.dp, CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                emoji,
                fontSize = (48 * scale).sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Gradient Progress Ring ───
@Composable
fun GradientProgressRing(
    progress: Float,
    size: Dp = 100.dp,
    strokeWidth: Dp = 10.dp,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = strokeWidth,
            color = Primary,
            trackColor = Primary.copy(alpha = 0.1f)
        )
        content()
    }
}

// ─── Decorative Background ───
@Composable
fun DecorativeBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 250.dp, y = (-50).dp)
                .background(Primary.copy(alpha = 0.04f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = 600.dp)
                .background(Secondary.copy(alpha = 0.03f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = 320.dp, y = 400.dp)
                .background(Tertiary.copy(alpha = 0.04f), CircleShape)
        )
    }
}
