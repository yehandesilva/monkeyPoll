import {useState} from 'react';

import {Dialog} from 'primereact/dialog';
import {Button} from 'primereact/button';

import Login from "./Login.jsx";
import Register from "./Register.jsx";

const LoginDialog = ({visible, setVisible}) => {

    const [isLogin, setIsLogin] = useState(true);

    return (
        <>
            <Dialog visible={visible} style={{minWidth: '30vw'}} draggable={false} resizable={false} onHide={() => {
                setVisible(false);
            }}>
                <div className="flex flex-column gap-2">
                    <div className="flex flex-column justify-content-center align-items-center gap-4 h-full">
                        <img src="monkeyPollLogoWithWhiteText.png" alt="MonkeyPoll Logo"
                             style={{width: '80%', maxWidth: '400px', height: 'auto'}}/>
                    </div>
                    <div className="flex justify-content-center align-items-center mb-5">
                        <span
                            className="font-medium">{(isLogin) ? "Don't have an account?" : "Already have an account?"}</span>
                        <Button text onClick={() => setIsLogin(!isLogin)} className="px-2"
                                style={{boxShadow: "none"}}>{(isLogin) ? "Create!" : "Login!"}</Button>
                    </div>
                    {
                        (isLogin) ?
                            <Login setVisible={setVisible}/>
                            :
                            <Register setVisible={setVisible}/>
                    }
                </div>
            </Dialog>
        </>
    )
}

export default LoginDialog;