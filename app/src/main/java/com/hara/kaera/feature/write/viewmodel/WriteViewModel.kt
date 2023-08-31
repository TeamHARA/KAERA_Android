package com.hara.kaera.feature.write.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.usecase.GetTemplateDetailUseCase
import com.hara.kaera.feature.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val detailUseCase: GetTemplateDetailUseCase
) : ViewModel() {


    private val _templateDetailFlow = MutableStateFlow<UiState<TemplateDetailEntity>>(UiState.Init)
    val templateDetailFlow = _templateDetailFlow.asStateFlow()

    private val _templateIdFlow = MutableStateFlow(-1)
    val templateIdFlow = _templateIdFlow.asStateFlow()

    private val _curTemplateIdFlow = MutableStateFlow(CurId.INIT.value)
    val curTemplateIdFlow = _curTemplateIdFlow.asStateFlow()
    //repeatOnLifeCycle에 따라서 백그라운드로 나갔다 다시 create되면
    //onStarted에서 tmplateIdFlow를 다시 collect함 그에 따라서
    // getTemplateDetailData함수를 재호출함
    // 그에따라 새로 render함수도 호출 되고 이전에 작성한 글이 render가 다시 되어서 작성했던 글들이 삭제되어 버림
    // 이를 방지하기 위해서 나갔다 들어와도 재호출되지 않도록 singleSelecteAdapter 처럼 이전 선택한
    // id를 저장해두었다가 조건검사에 써준다
    fun setTemplateId(choiceId: Int) {
        _templateIdFlow.value = choiceId
    }

    fun setCurTemplateId(choiceId: Int){
        _curTemplateIdFlow.value = choiceId
    }

    fun getTemplateDetailData() {
        _templateDetailFlow.value = UiState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                detailUseCase(templateIdFlow.value)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _templateDetailFlow.value = UiState.Success(collect.data)
                            _curTemplateIdFlow.value = CurId.INIT.value
                        }

                        is ApiResult.Error -> {
                            _templateDetailFlow.value = UiState.Error(collect.error.toString())
                        }
                    }
                }
            }.onFailure {
                throw it
            }
        }
    }

    enum class CurId(val value : Int) {
        INIT(-1)
    }

    // 여기는 구조가 나중에라도 바뀔수 있다 하지만 이거 기반대로 얘기를 해보자면
    // ViewModelScope로 비동기동작을 위한 코루틴 열고
    // runCatching에서 주입받은 UseCase를 사용한다 그러면 UseCase에 의해서 Repository -> DataSoure -> Api인터페이스 기반 Retrofit 실행이 되어서
    // 서버통신이 되겠지?
    // 그 이후에 성공하면 다음과 같이 Flow가 리턴이 된다. 그러면 collect를 통해서 수집
    // *레포짓토리, DataSource 에서 해준 emit이 flow라는 강줄기에 데이터를 뛰운거고 그게 흘러흘러 ViewModel에서 collect를 통해서 수집한거라 생각하면 된다.*
    // 그 이후 내가 설계한 Entity대로라면 templateTypeList가 null이면 오류이므로 UiState를 Error로 바꾸어주고
    // 아니면 UiState Success에 실제 데이터를 담아준다.
    // 이렇게 되면 그 이후 과정은 실제 View에서 계속..


}