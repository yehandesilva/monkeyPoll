import {useContext, useRef, useState} from 'react';

import {Button} from 'primereact/button';

import LoginDialog from "./welcome/LoginDialog.jsx";
import {UserContext} from "./context/UserContext.jsx";
import {logoutUser} from "./api/userApi.js";


const MenuBar = () => {
    const toast = useRef(null);
    const [loginVisible, setLoginVisible] = useState(false);
    const [user, setUser] = useContext(UserContext);

    const handleLogout = async () => {

        const logoutStatus = await logoutUser();
        if (logoutStatus.success) {
            setUser(null);
        } else {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Logout Error',
                detail: logoutStatus.body.message,
            });
        }
    };

    return (
        <>
            <LoginDialog visible={loginVisible} setVisible={setLoginVisible} />
            {user ? (
                <Button label="Logout" icon="pi pi-sign-out" size="small" className="absolute top-0 right-0 m-4"
                        style={{ boxShadow: "none" }} onClick={handleLogout} />
            ) : (
                <Button label="Login" icon="pi pi-sign-in" size="small" className="absolute top-0 right-0 m-4"
                        style={{ boxShadow: "none" }} onClick={() => setLoginVisible(true)} />
            )}
        </>
    );
}

export default MenuBar;