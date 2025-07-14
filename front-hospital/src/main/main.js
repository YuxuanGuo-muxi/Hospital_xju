import { app, BrowserWindow, ipcMain, Menu } from 'electron';
import { dirname, join } from 'path';
import { fileURLToPath } from 'url';

let __filename = fileURLToPath(import.meta.url);
let __dirname = dirname(__filename);

let mainWindow;
let needsFocusFix = false;
let triggeringProgrammaticBlur = false;

const isWindows = process.platform === 'win32';

const createWindow = () => {
    Menu.setApplicationMenu(null);
    mainWindow = new BrowserWindow({
        // ...窗口配置
        width: 1600,
        height: 900,
        center: true,
        minWidth: 1600,
        minHeight: 900,
        icon: './src/renderer/assets/picture/logo.png', // 指定图标路径

        webPreferences: {
            preload: join(__dirname, 'preload.mjs'),
            nodeIntegration: true,
            contextIsolation: true,
            webSecurity: true
        }
    });

    mainWindow.loadFile('./web/index.html')

    // 添加 blur 和 focus 事件监听
    mainWindow.on('blur', (event) => {
        if (!triggeringProgrammaticBlur) {
            needsFocusFix = true;
        }
    });

    mainWindow.on('focus', (event) => {
        if (isWindows && needsFocusFix) {
            needsFocusFix = false;
            triggeringProgrammaticBlur = true;
            setTimeout(() => {
                mainWindow.blur();
                mainWindow.focus();
                setTimeout(() => {
                    triggeringProgrammaticBlur = false;
                }, 100);
            }, 100);
        }
    });
};


app.whenReady().then(() => {
    createWindow()

    // 关闭窗体
    ipcMain.on("close-window", (event) => {
        const webContents = event.sender;
        const win = BrowserWindow.fromWebContents(webContents);
        if (win) {
            if (gameExec.process !== null) {
                const size = {
                    width: 610,
                    height: 200,
                };
                const url = "http://localhost:3000/subwin/messagebox";
                const content = {
                    name: gameExec.name,
                    icon: gameExec.icon,
                };
                const contentStr = JSON.stringify(content);
                console.log("message-content:" + contentStr);
                createMessageBox({
                    mainWindow: mainWindow,
                    size: size,
                    url: url,
                    content: content,
                    hasmask: true,
                    callback: () => {},
                });
            } else {
                win.close();
            }
        }
    });

    // 最大化窗体
    ipcMain.on("max-window", (event) => {
        const webContents = event.sender;
        const win = BrowserWindow.fromWebContents(webContents);
        win.maximize();
    });

    // 还原窗体
    ipcMain.on("unmax-window", (event) => {
        const webContents = event.sender;
        const win = BrowserWindow.fromWebContents(webContents);
        win.unmaximize();
    });

    // 最小化窗体
    ipcMain.on("min-window", (event) => {
        const webContents = event.sender;
        const win = BrowserWindow.fromWebContents(webContents);
        win.minimize();
    });

    app.on('activate', () => {
        if (BrowserWindow.getAllWindows().length === 0) createWindow()
    })
})


app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') app.quit()
})