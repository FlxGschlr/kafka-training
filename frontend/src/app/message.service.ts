import {Injectable, NgZone} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private backendUrl: string = 'http://localhost:8080';
  private eventSource: EventSource;
  private messageDataSource: BehaviorSubject<Array<string>> = new BehaviorSubject([]);

  messageData = this.messageDataSource.asObservable();

  constructor(private zone: NgZone) {}

  public startMessageInfoEventSource(): void {
    let url = [this.backendUrl, 'messages'].join('/');

    this.eventSource = new EventSource(url);
    this.eventSource.onmessage = (event) => {

      console.log('got event data', event['data']);
      const newArrays = [...this.messageDataSource.value, event['data']];

      this.zone.run(() => {
        this.messageDataSource.next(newArrays);
      })

    }

    this.eventSource.onerror = (error) => {

      this.zone.run( () => {
        // readyState === 0 (closed) means the remote source closed the connection,
        // so we can safely treat it as a normal situation. Another way of detecting the end of the stream
        // is to insert a special element in the stream of events, which the client can identify as the last one.
        if(this.eventSource.readyState === 0) {
          this.eventSource.close();
          this.messageDataSource.complete();
        } else {
          this.messageDataSource.error('EventSource error: ' + error);
        }
      });
    }
  }

  public onClose() {
    this.eventSource.close();
    this.messageDataSource.complete();

  }
}
