package com.linkzip.linkzip.presentation.feature.group

import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.linkzip.linkzip.R
import com.linkzip.linkzip.data.room.GroupData
import com.linkzip.linkzip.data.room.IconData
import com.linkzip.linkzip.data.room.LinkData
import com.linkzip.linkzip.presentation.component.BottomDialogMenuComponent
import com.linkzip.linkzip.presentation.component.HeaderTitleView
import com.linkzip.linkzip.presentation.component.LinkGroupComponent
import com.linkzip.linkzip.presentation.component.swipeLinkGroupComponent
import com.linkzip.linkzip.presentation.feature.addgroup.IconView
import com.linkzip.linkzip.presentation.feature.addgroup.getDrawableIcon
import com.linkzip.linkzip.ui.theme.LinkZipTheme

@Composable
fun GroupView(
    groupData: Pair<GroupData, IconData>?,
    onBackButtonPressed: () -> Unit,
    onActionButtonPressed: () -> Unit,
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    val backgroundColor = groupData?.second?.iconHeaderColor
    val groupName = groupData?.first?.groupName

    LaunchedEffect(groupData) {
        groupViewModel.getLinkListByGroup(groupData?.first?.groupId ?: throw NullPointerException())
    }

    val linkList by groupViewModel.linkListByGroup.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        HeaderTitleView(
            backgroundColor = Color(backgroundColor ?: LinkZipTheme.color.white.toArgb()),
            onBackButtonPressed = onBackButtonPressed,
            onActionButtonPressed = onActionButtonPressed,
            title = groupName ?: "error"
        )
        Spacer(modifier = Modifier.height(32.dp))
        TextWithIcon(
            modifier = Modifier.padding(start = 22.dp),
            iconFile = R.drawable.icon_pin,
            message = stringResource(R.string.favorite_link)
        )
        Box(modifier = Modifier.height(8.dp))
        LazyColumn {
            itemsIndexed(linkList) { index, data ->
                LinkInGroup(data)
            }
        }
    }
}


@Composable
fun TextWithIcon(modifier: Modifier, iconFile: Int, message: String) {
    Row(modifier = modifier) {
        Icon(
            painter = painterResource(id = iconFile),
            contentDescription = "favorite",
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = message,
            style = LinkZipTheme.typography.medium12.copy(color = LinkZipTheme.color.wg70)
        )
    }
}

@Composable
fun LinkInGroup(link: LinkData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                modifier = Modifier
                    .width(128.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null
            )
            Box(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = link.linkTitle,
                    style = LinkZipTheme.typography.bold14.copy(color = LinkZipTheme.color.wg70)
                )
                Box(modifier = Modifier.height(8.dp))
                Text(
                    text = "메모 추가하기",
                    style = LinkZipTheme.typography.medium12.copy(color = LinkZipTheme.color.wg50),
                    textDecoration = TextDecoration.Underline
                )
            }
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.icon_threedots),
                contentDescription = null
            )
        }

    }
}