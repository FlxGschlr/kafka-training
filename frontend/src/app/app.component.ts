import {Component, HostListener} from '@angular/core';
import {Observable} from "rxjs";
import {MessageService} from "./message.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';

  messageInfo$: Observable<string[]>;
  all: string[];

  constructor(private service: MessageService) { }

  ngOnInit() {
    this.service.startMessageInfoEventSource();
    this.messageInfo$ = this.service.messageData;
  }

  ngOnDestroy() {
    this.service.onClose();
  }

  @HostListener('window:beforeunload', [ '$event' ])
  unloadHandler(event) {
    console.log('unloadHandler');
    this.service.onClose();
  }
}
