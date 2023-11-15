export interface User{
  username: string;
  name: string;
  password: string;
  address: string;
  email: string;
  cui: number;
  birthDate: string;
  type: number; // 1: admin, 2: empresa, 3: usuario
  profileCompleted: boolean;
}