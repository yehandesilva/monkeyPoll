import {useState} from 'react';

import PropTypes from 'prop-types';
import {InputText} from 'primereact/inputtext';
import {Password} from 'primereact/password';
import {Button} from 'primereact/button';

import {registerUser} from "../api/userApi.js";
import {validateEmail} from "../modules/validation.js";

const Register = ({toast, setIsLogin}) => {

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [invalidFields, setInvalidFields] = useState({
        firstName: false,
        lastName: false,
        email: false,
        password: false,
    });

    const onSubmit = async () => {
        const _invalidFields = {...invalidFields};
        if (firstName && lastName && email && password) {
            if (validateEmail(email)) {
                const registerStatus = await registerUser(firstName, lastName, email, password);
                if (registerStatus.success) {
                    toast.current.show({
                        severity: 'success',
                        life: 3000,
                        summary: 'Registration Success',
                        detail: 'Account Created',
                    });
                    setIsLogin(true);
                } else {
                    toast.current.show({
                        severity: 'error',
                        life: 3000,
                        summary: 'Registration Error',
                        detail: registerStatus.body.message,
                    });
                }
            } else {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Registration Error',
                    detail: 'Email must be in format of example@email.com'
                });
                _invalidFields.email = true;
            }
        } else {
            // Sign-in fields are empty
            if (!firstName) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Registration Error',
                    detail: 'First name is required'
                });
            }
            if (!lastName) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Registration Error',
                    detail: 'Last name is required'
                });
            }
            if (!email) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Registration Error',
                    detail: 'Email is required'
                });
            }
            if (!password) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Registration Error',
                    detail: 'Password is required'
                });
            }
            _invalidFields.firstName = !firstName;
            _invalidFields.lastName = !lastName;
            _invalidFields.email = !email;
            _invalidFields.password = !password;
        }
        setInvalidFields({..._invalidFields});
    }

    return (
        <>
            <div className="flex justify-content-center pb-6">
                <div className="flex flex-column max-w-max">
                    <div>
                        <label htmlFor="First Name" className="block mb-1 ml-1">First Name</label>
                        <InputText value={firstName} onChange={(e) => setFirstName(e.target.value)}
                                   invalid={invalidFields.firstName} className="w-full"/>
                    </div>
                    <div className="mt-3">
                        <label htmlFor="Last Name" className="block mb-1 ml-1">Last Name</label>
                        <InputText value={lastName} onChange={(e) => setLastName(e.target.value)}
                                   invalid={invalidFields.lastName} className="w-full"/>
                    </div>
                    <div className="mt-3">
                        <label htmlFor="email" className="block mb-1 ml-1">E-mail</label>
                        <InputText value={email} onChange={(e) => setEmail(e.target.value)}
                                   invalid={invalidFields.email} className="w-full"/>
                    </div>
                    <div className="mt-3">
                        <label htmlFor="password" className="block mb-1 ml-1">Password</label>
                        <Password value={password} toggleMask invalid={invalidFields.password}
                                  onChange={(e) => setPassword(e.target.value)}/>
                    </div>
                    <div className="flex justify-content-center mt-6">
                        <Button label="Register" icon="pi pi-user-plus" onClick={() => onSubmit()}
                                className="max-w-min"/>
                    </div>
                </div>
            </div>
        </>
    )
}

Register.propTypes = {
    toast: PropTypes.object.isRequired,
    setIsLogin: PropTypes.func.isRequired
};

export default Register;