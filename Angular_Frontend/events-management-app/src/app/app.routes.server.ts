import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: 'event-registration/:id',
    renderMode: RenderMode.Prerender,
    getPrerenderParams: async () => {
      // Generate event IDs to prerender (adjust based on your events)
      // This creates URLs like: /event-registration/1, /event-registration/2, etc.
      const eventIds = Array.from({ length: 50 }, (_, i) => ({ id: String(i + 1) }));
      return eventIds;
    }
  },
  {
    path: '**',
    renderMode: RenderMode.Prerender
  }
];
