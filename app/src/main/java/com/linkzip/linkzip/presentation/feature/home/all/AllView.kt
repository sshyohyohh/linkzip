package com.linkzip.linkzip.presentation.feature.home.all

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.linkzip.linkzip.R
import com.linkzip.linkzip.common.UiState
import com.linkzip.linkzip.data.room.GroupData
import com.linkzip.linkzip.presentation.component.IntroduceComponent
import com.linkzip.linkzip.presentation.component.LinkGroupComponent
import com.linkzip.linkzip.presentation.feature.home.HomeViewModel
import com.linkzip.linkzip.ui.theme.LinkZipTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun AllView (
    homeViewModel: HomeViewModel =  hiltViewModel(),
    dimmedBoolean: (Boolean) ->Unit,
    onClickAddGroup: ()->Unit
){
    var dimmedBackground by remember { mutableStateOf(false) }
    val groupEvent by homeViewModel.allGroupListFlow.collectAsStateWithLifecycle(null)

    var randomColors = listOf(
        LinkZipTheme.color.orangeFFE6C1,
        LinkZipTheme.color.greenBDF3C2,
        LinkZipTheme.color.pinkFFE8F7,
        LinkZipTheme.color.blueC0F0FF
    )

    LaunchedEffect(dimmedBackground){
        CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.setBackgroundDim(dimmedBackground)
        }
    }

    LaunchedEffect(true){
        CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.getAllGroups()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        if (groupEvent != null){
            GroupList(groupEvent!!)
        }

        Box(
            modifier= Modifier
                .clickable { onClickAddGroup.invoke() }
                .fillMaxWidth(1f)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 22.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LinkZipTheme.color.wg10),
            contentAlignment = Alignment.Center
        ){
            Text(text =  AnnotatedString(stringResource(R.string.add_group)),
                style = LinkZipTheme.typography.normal16.copy(color = LinkZipTheme.color.wg50))
        }
    }

    IntroduceComponent{ isDimmed ->
        dimmedBackground = isDimmed
        dimmedBoolean(dimmedBackground)
    }

}

@Composable
fun GroupList(
    state : UiState<List<GroupData>>
){

    when(state){
        is UiState.Success ->{
            LazyColumn(){
                items(state.data){ group ->
                    LinkGroupComponent(
                        group.groupName,
                        R.drawable.guide_image,
                        LinkZipTheme.color.orangeFFE6C1,
                        1L
                    ){ it ->
                        Log.e("groupClick" , "$it")
                    }
                }
            }
        }
        is UiState.Loding -> {

        }
        is UiState.Error -> {
            Log.e("group List Error" , state.error?.message.toString())
        }
    }
}