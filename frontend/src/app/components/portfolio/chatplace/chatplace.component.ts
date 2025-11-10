import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from "@angular/common";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { WebsocketService } from "../../../services/websocket/websocket.service";
import { IMessage, StompSubscription } from "@stomp/stompjs";

@Component({
  selector: 'app-chatplace',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './chatplace.component.html',
  styleUrl: './chatplace.component.scss'
})
export class ChatplaceComponent implements OnInit, OnDestroy {
  chatForm: FormGroup;
  messages: string[] = [];
  private topicSubscription: StompSubscription | undefined;

  constructor(
    private fb: FormBuilder,
    private websocketService: WebsocketService
  ) {
    this.chatForm = this.fb.group({
      message: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.websocketService.connect();
    this.websocketService.connectionState$.subscribe(state => {
      if (state === 'CONNECTED') {
        this.topicSubscription = this.websocketService.subscribe('/topic/reply', (message: IMessage) => {
          this.messages.push(message.body);
        });
      }
    });
  }

  ngOnDestroy(): void {
    this.topicSubscription?.unsubscribe();
    this.websocketService.disconnect();
  }

  sendMessage(): void {
    if (this.chatForm.valid) {
      const message = this.chatForm.value.message;
      this.websocketService.sendMessage('/app/message', message);
      this.chatForm.reset();
    }
  }
}
