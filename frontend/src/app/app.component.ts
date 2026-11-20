import { Component } from '@angular/core';
import { PortalPagosComponent } from './components/portal-pagos/portal-pagos.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [PortalPagosComponent],
  templateUrl: './app.component.html'
})
export class AppComponent {}
