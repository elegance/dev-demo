1. 建立目录，并下载 bundle

   ```bash
   cd /usr/share/vim/vimfiles/
   mkdir bundle
   cd bundle
   git clone https://github.com/gmarik/vundle
   ```

2. 建立配置文件`vim ~/.vimrc`, 内容见文件 [.vimrc](.vimrc)

3. 打开`vim`, 命令模式：`:BundleInstall`

4. `ctags`的安装, http://ctags.sourceforge.net/

   ```bash
   wget http://prdownloads.sourceforge.net/ctags/ctags-5.8.tar.gz
   tar -zxvf ctags-5.8.tar.gz
   ./configure && make && make install
   ```

   ​

