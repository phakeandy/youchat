package top.phakeandy.youchat.message;

@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface MessageService {

  void saveMessage(MessageRequest message);
}
