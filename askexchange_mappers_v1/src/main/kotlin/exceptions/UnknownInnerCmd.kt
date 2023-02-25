package exceptions

import models.InnerCommand

class UnknownInnerCmd(command: InnerCommand): Throwable("Wrong command $command at toTransport stage mapping")