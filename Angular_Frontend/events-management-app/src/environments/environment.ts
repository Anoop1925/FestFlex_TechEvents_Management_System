export const environment = {
  production: false,
  apiUrl: 'https://festflex-backend-ap.onrender.com',
  geminiApiKey: '', // Will be handled by backend for security
  appName: 'FestFlex',
  version: '1.0.0',
  chatbot: {
    maxRetries: 3,
    retryDelay: 1000,
    timeoutMs: 30000
  }
};