package exceptions

import ru.shirnin.askexchange.inner.models.InnerCommand

class UnknownInnerCmd(command: InnerCommand): Throwable("Wrong command $command at toTransport stage mapping")