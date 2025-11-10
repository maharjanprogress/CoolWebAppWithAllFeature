/***************************************************************************************************
 * BROWSER POLYFILLS
 */

/**
 * SockJS client needs a global variable.
 */
(window as any).global = window;
