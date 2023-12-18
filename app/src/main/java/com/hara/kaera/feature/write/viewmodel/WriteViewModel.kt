package com.hara.kaera.feature.write.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.EditWorryReqDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.domain.usecase.EditWorryUseCase
import com.hara.kaera.domain.usecase.GetTemplateDetailUseCase
import com.hara.kaera.domain.usecase.GetWorryDetailUseCase
import com.hara.kaera.domain.usecase.WriteWorryUseCase
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToLayout
import com.hara.kaera.feature.util.errorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val detailUseCase: GetTemplateDetailUseCase,
    private val writeWorryUseCase: WriteWorryUseCase,
    private val editWorryUseCase: EditWorryUseCase,
    private val worryUseCase: GetWorryDetailUseCase,
) : ViewModel() {

    private val _templateDetailFlow = MutableStateFlow<UiState<TemplateDetailEntity>>(UiState.Init)
    val templateDetailFlow = _templateDetailFlow.asStateFlow()

    // 템플릿 선택 바텀시트로부터 가져오는 ShortInfo
    private val _templateShortInfo = MutableStateFlow("")
    val templateShortInfo = _templateShortInfo.asStateFlow()

    // templateId : 서버통신 하는 게 아니라 LiveData 대용으로 쓴 것
    private val _templateIdFlow = MutableStateFlow(-1)
    val templateIdFlow = _templateIdFlow.asStateFlow()
    private val _curTemplateIdFlow = MutableStateFlow(CurId.INIT.value)
    val curTemplateIdFlow = _curTemplateIdFlow.asStateFlow()

    // 글 작성 결과
    private val _writeWorryFlow = MutableStateFlow<UiState<String>>(UiState.Init)
    val writeWorryFlow = _writeWorryFlow.asStateFlow()

    // 글 수정 결과
    private val _editWorryFlow = MutableStateFlow<UiState<String>>(UiState.Init)
    val editWorryFlow = _editWorryFlow.asStateFlow()

    /* 글 수정 시에만 사용 */
    private var worryId = -1
    private val _detailStateFlow = MutableStateFlow<UiState<WorryDetailEntity>>(UiState.Init)
    val detailStateFlow = _detailStateFlow.asStateFlow()

    //repeatOnLifeCycle에 따라서 백그라운드로 나갔다 다시 create되면
    //onStarted에서 tmplateIdFlow를 다시 collect함 그에 따라서
    // getTemplateDetailData함수를 재호출함
    // 그에따라 새로 render함수도 호출 되고 이전에 작성한 글이 render가 다시 되어서 작성했던 글들이 삭제되어 버림
    // 이를 방지하기 위해서 나갔다 들어와도 재호출되지 않도록 singleSelecteAdapter 처럼 이전 선택한
    // id를 저장해두었다가 조건검사에 써준다

    fun setWorryId(worryId: Int) {
        this.worryId = worryId
    }

    fun getWorryId(): Int {
        return worryId
    }

    fun setTemplateId(choiceId: Int) {
        _templateIdFlow.value = choiceId
    }

    fun setCurTemplateId(choiceId: Int) {
        _curTemplateIdFlow.value = choiceId
    }

    fun setTemplateData(choiceId: Int, shortInfo: String){
        _templateIdFlow.value = choiceId
        _templateShortInfo.value = shortInfo
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
                            _templateDetailFlow.value = UiState.Error(errorToLayout(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw it
            }
        }
    }

    fun writeWorry(writeWorryReqDTO: WriteWorryReqDTO) {
        _writeWorryFlow.value = UiState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                writeWorryUseCase(writeWorryReqDTO)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _writeWorryFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _writeWorryFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }

    fun editWorry(editWorryReqDTO: EditWorryReqDTO) {
        _editWorryFlow.value = UiState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                editWorryUseCase(editWorryReqDTO)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _editWorryFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _editWorryFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }

    // [글 수정 시에만 사용] 글 detail 조회
    fun getWorryDetail() {
        viewModelScope.launch {
            _detailStateFlow.value = UiState.Loading
            kotlin.runCatching {
                worryUseCase(worryId)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _detailStateFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _detailStateFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }

    enum class CurId(val value: Int) {
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