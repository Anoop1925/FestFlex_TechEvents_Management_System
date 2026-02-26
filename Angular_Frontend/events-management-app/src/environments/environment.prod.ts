export const environment = {
  production: true,
  apiUrl: 'https://festflex-backend-production.up.railway.app',
  geminiApiKey: '', // Will be handled by backend for security
  appName: 'FestFlex',
  version: '1.0.0',
  chatbot: {
    maxRetries: 3,
    retryDelay: 1000,
    timeoutMs: 30000
  }
};