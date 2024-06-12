package com.chatapp.chat;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Controller
public class ChatController {

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		return chatMessage;
	}
	
	@MessageMapping("chat.addUser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}
	
	@PostMapping("/app/createOrder")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
        // Handle the creation of Razorpay order
        System.out.println("Received data: " + data);

        int amount = Integer.parseInt(data.get("amount").toString());
        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_HrOR8OC9NXBimg", "zgwwiqvAs3D4M4ENwUumxiMW");

        JSONObject obj = new JSONObject();
        obj.put("amount", amount * 100); // Convert to paisa
        obj.put("currency", "INR");
        obj.put("receipt", "txn_687324"); // Receipt ID

        Order order = razorpayClient.Orders.create(obj);
        System.out.println("Created order: " + order);

        return order.toString(); // Assuming you want to return the order details
    }
}
