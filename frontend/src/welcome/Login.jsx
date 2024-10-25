import {useState, useRef, useContext} from 'react';

import {Toast} from 'primereact/toast';
import {InputText} from 'primereact/inputtext';
import {Password} from 'primereact/password';
import {Button} from 'primereact/button';

import {UserContext} from "../context/UserContext.jsx";
import {validateEmail} from "../modules/validation.js";
import {loginUser} from "../api.js";


const Login = ({setVisible}) => {

    const toast = useRef(null);
    const [user, setUser] = useContext(UserContext);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [invalidFields, setInvalidFields] = useState({
        email: false,
        password: false,
    });

    const onSubmit = async () => {
        const _invalidFields = {...invalidFields};
        if (email && password) {
            // Validate email format
            if (validateEmail(email)) {
                const loginStatus = await loginUser(email, password);
                if (loginStatus.success) {
                    setUser(loginStatus.body);
                    setVisible(false);
                } else {
                    toast.current.show({
                        severity: 'error',
                        life: 3000,
                        summary: 'Login Error',
                        detail: loginStatus.body.message,
                    });
                }
            } else {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Login Error',
                    detail: 'Email must be in format of example@email.com'
                });
                _invalidFields.email = true;
            }
        } else {
            // Email or password fields are empty
            if (!email) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Login Error',
                    detail: 'Email is required'
                });
            }
            if (!password) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Login Error',
                    detail: 'Password is required'
                });
            }
            _invalidFields.email = !email;
            _invalidFields.password = !password;
        }
        setInvalidFields({..._invalidFields});
    }

    return (
        <>
            <Toast ref={toast}/>
            <div className="flex justify-content-center pb-6">
                <div className="flex flex-column max-w-max">
                    <div>
                        <label htmlFor="email" className="block mb-1 ml-1">E-mail</label>
                        <InputText value={email} onChange={(e) => setEmail(e.target.value)}
                                   invalid={invalidFields.email} className="w-full"/>
                    </div>
                    <div className="mt-3">
                        <label htmlFor="password" className="block mb-1 ml-1">Password</label>
                        <Password value={password} toggleMask feedback={false} invalid={invalidFields.password}
                                  onChange={(e) => setPassword(e.target.value)}/>
                    </div>
                    <div className="flex justify-content-center mt-6">
                        <Button label="Login" icon="pi pi-sign-in" onClick={() => onSubmit()} className="max-w-min"/>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Login;