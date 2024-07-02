package chatbot.infra;

import chatbot.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/chats")
@Transactional
public class ChatController {

    @Autowired
    ChatRepository chatRepository;

    @RequestMapping(
        value = "chats/{id}/question",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Chat question(
        @PathVariable(value = "id") Long id,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /chat/question  called #####");
        Optional<Chat> optionalChat = chatRepository.findById(id);

        optionalChat.orElseThrow(() -> new Exception("No Entity Found"));
        Chat chat = optionalChat.get();
        chat.question();

        chatRepository.save(chat);
        return chat;
    }

    @RequestMapping(
        value = "chats/{id}/request",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Chat request(
        @PathVariable(value = "id") Long id,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /chat/request  called #####");
        Optional<Chat> optionalChat = chatRepository.findById(id);

        optionalChat.orElseThrow(() -> new Exception("No Entity Found"));
        Chat chat = optionalChat.get();
        chat.request();

        chatRepository.save(chat);
        return chat;
    }
}
//>>> Clean Arch / Inbound Adaptor
