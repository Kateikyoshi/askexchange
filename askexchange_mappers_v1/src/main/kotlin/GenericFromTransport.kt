import ru.shirnin.askexchange.inner.models.InnerWorkMode
import ru.shirnin.askexchange.api.v1.models.Debug
import ru.shirnin.askexchange.api.v1.models.RequestDebugMode
import ru.shirnin.askexchange.api.v1.models.RequestDebugStubs
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs

fun Debug.transportToWorkMode(): InnerWorkMode = when (this.mode) {
    RequestDebugMode.PROD -> InnerWorkMode.PROD
    RequestDebugMode.TEST -> InnerWorkMode.TEST
    RequestDebugMode.STUB -> InnerWorkMode.STUB
    null -> InnerWorkMode.PROD
}

fun Debug.transportToStubCase(): InnerStubs = when (this.stub) {
    RequestDebugStubs.SUCCESS -> InnerStubs.SUCCESS
    RequestDebugStubs.NOT_FOUND -> InnerStubs.NOT_FOUND
    RequestDebugStubs.BAD_ID -> InnerStubs.BAD_ID
    RequestDebugStubs.BAD_TITLE -> InnerStubs.BAD_TITLE
    RequestDebugStubs.CANNOT_DELETE -> InnerStubs.CANNOT_DELETE
    null -> InnerStubs.NONE
}