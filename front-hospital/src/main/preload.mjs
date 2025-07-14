import { contextBridge, ipcRenderer, shell } from 'electron'


contextBridge.exposeInMainWorld('electron', {
    ipcRenderer,
    shell
})

contextBridge.exposeInMainWorld('electronAPI', {
    closeWindow: () => ipcRenderer.send('close-window'),
    maxWindow: () => ipcRenderer.send('max-window'),
    unmaxWindow: () => ipcRenderer.send('unmax-window'),
    minWindow: () => ipcRenderer.send('min-window')
})
