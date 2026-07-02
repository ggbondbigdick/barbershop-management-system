import {JSEncrypt} from 'jsencrypt'


// RES 非对称加密
export const encryptByPublicKey = (password: string): string => {

    // 公钥 （写死或者接口获取）
    const publicKey = 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzz/fF+zvqbMKFzovQH0J8963ecF6AZc8wyQ/N+t63wzyV8LTez7GBHPit0+A6pCAHcHUMNNRTLGSVbtf9fZuh1mkfZzrmxOvsPueHvs1U7gwBioFIxtsLzDMQKEltt71F9Gc5OeLn5PZzZ1ykAaMFyRBWEuaAu6VB3NYpTzrnfLs+SXE49byp63aFcldJaKxdkmATyqHo6W0up/+Pr7GSekrs5P3535/nMFk7T9jOXIJbn6p9OYede/2YLI33mdrBmh0TbJO+c1nAPDwOASXATb31dTVdgOb88c1c510dB5xulncXLnYTDAaxXzTwWH+lkLpv1k2EbrbOLzJE/6cIQIDAQAB'

    // 新建  JSEncrypt 对象
    let encrypt = new JSEncrypt()

    // 设置公钥
    encrypt.setPublicKey(publicKey)

    return <string>encrypt.encrypt(password)
}
