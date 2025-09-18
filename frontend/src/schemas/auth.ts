import { z } from 'zod'

// Login form schema
export const loginSchema = z.object({
  email: z.email({ message: '请输入有效的邮箱地址' }),
  password: z.string().min(6, '密码至少需要6个字符'),
})

// Register form schema
export const registerSchema = z
  .object({
    email: z.email('请输入有效的邮箱地址'),
    username: z.string().min(2, '用户名至少需要2个字符').max(20, '用户名最多20个字符'),
    password: z
      .string()
      .min(6, '密码至少需要6个字符')
      .regex(/[A-Z]/, '密码必须包含至少一个大写字母')
      .regex(/[a-z]/, '密码必须包含至少一个小写字母')
      .regex(/[0-9]/, '密码必须包含至少一个数字'),
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: '密码不匹配',
    path: ['confirmPassword'],
  })

// Export types
export type LoginForm = z.infer<typeof loginSchema>
export type RegisterForm = z.infer<typeof registerSchema>

// Error types
export type FormErrors<T> = Partial<Record<keyof T, string[]>>

// Validation utilities
export const validateLogin = (
  data: unknown,
): { success: true; data: LoginForm } | { success: false; errors: FormErrors<LoginForm> } => {
  const result = loginSchema.safeParse(data)
  if (result.success) {
    return { success: true, data: result.data }
  } else {
    return {
      success: false,
      errors: result.error.flatten().fieldErrors as FormErrors<LoginForm>,
    }
  }
}

export const validateRegister = (
  data: unknown,
): { success: true; data: RegisterForm } | { success: false; errors: FormErrors<RegisterForm> } => {
  const result = registerSchema.safeParse(data)
  if (result.success) {
    return { success: true, data: result.data }
  } else {
    return {
      success: false,
      errors: result.error.flatten().fieldErrors as FormErrors<RegisterForm>,
    }
  }
}
