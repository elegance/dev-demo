set tabstop=4                " 设置tab键的宽度                                    
set clipboard=unnamed,unnamedplus " 未命名复制 ，+ 命名复制 共享至系统复制板                                                        
set shiftwidth=4             " 换行时行间交错使用\个空格
set autoindent               " 自动对齐
set backspace=2              " 设置退格键可用
set cindent shiftwidth=4     " 自动缩进4空格
set nu                       " 显示行号
"set showmatch               " 显示括号配对情况
set mouse=a                  " 启用鼠标
set ruler                    " 右下角显示光标位置的状态行
set incsearch                " 开启实时搜索功能
set hlsearch                 " 开启高亮显示结果
set nowrapscan               " 搜索到文件两端时不重新搜索
set nocompatible             " 关闭兼容模式
set vb t_vb=                 " 关闭提示音
set cursorline              " 突出显示当前行
set hidden                   " 允许在有未保存的修改时切换缓冲区
set list                     " 显示Tab符，使用一高亮竖线代替
set listchars=tab:\|\ ,

syntax enable                " 打开语法高亮
syntax on                    " 开启文件类型侦测
filetype indent on           " 针对不同的文件类型采用不同的缩进格式
filetype plugin on           " 针对不同的文件类型加载对应的插件
filetype plugin indent on    " 启用自动补全

set writebackup              " 设置无备份文件
set nobackup
set autochdir                " 设定文件浏览器目录为当前目录

set foldmethod=syntax        " 选择代码折叠类型
set foldlevel=100            " 禁止自动折叠

set laststatus=2             " 开启状态栏信息
set cmdheight=2              " 命令行的高度，默认为1，这里设为2

set fenc=utf-8
set encoding=utf-8
set fileencodings=utf-8,gbk,cp936,latin-1

language messages zh_CN.utf-8

" ======= 引号 && 括号自动匹配 ======= "


":inoremap ( ()<ESC>i
"
":inoremap ) <c-r>=ClosePair(')')<CR>
"
":inoremap { {}<ESC>i
"
":inoremap } <c-r>=ClosePair('}')<CR>
"
":inoremap [ []<ESC>i

":inoremap ] <c-r>=ClosePair(']')<CR>
"
":inoremap " ""<ESC>i
"
":inoremap ' ''<ESC>i
"
":inoremap ` ``<ESC>i

function! ClosePair(char)
|   if getline('.')[col('.') - 1] == a:char
|   |   return "\<Right>"
|   else
|   |   return a:char
|   endif
endf

"set rtp+=~/.vim/bundle/vundle
set rtp+=/usr/share/vim/vimfiles/bundle/vundle
"call vundle#begin()
call vundle#rc()

Bundle 'gmarik/vundle'
Bundle 'msanders/snipmate.vim'
Bundle 'mattn/emmet-vim'
"#-> js 检查,Node.js must be in your path.  nodejs.org
"Bundle 'wookiehangover/jshint.vim'
Bundle 'walm/jshint.vim'
"#支持多重选取内容的 Vim 插件 https://github.com/terryma/vim-multiple-cursors
Bundle 'terryma/vim-multiple-cursors'
""增加了rgb显示颜色和同行显示多处颜色
Bundle 'skammer/vim-css-color'

"格式②：vim-scripts里面的仓库，直接打仓库名即可。
""#-> vim脚本，提供了一些实用函数和命令,其它插件有可能依赖它
Bundle 'L9'
"#-> 
Bundle 'ctags.vim'
Bundle 'taglist.vim'
"#-> VIM中的文件查找利器
Bundle 'FuzzyFinder'    
"#-> 可以打开历史文件列表以达到快速切换文件的目的
Bundle 'bufexplorer.zip' 
""#-> 高亮显示多个单词
Bundle 'Mark'    
"#-> 打开大文件
Bundle 'LargeFile'
let g:LargeFile=10 "把大文件的标准定义为10MB
"#-> 菜单
Bundle 'The-NERD-tree'

" 按键映射/以及插件配置
"
" NERDTREE
let NERDTreeWinPos = "left" "where NERD tree window is placed on the screen                                                                                                 
let NERDTreeIgnore = ['node_modules[[dir]]', 'target[[dir]]', '\.class$[[file]]']
nmap <C-i> :NERDTreeToggle<CR>" Open and close the NERD_tree.vim separately
" 让  fuzzy finder 的 候选打开文件中,不再考虑 dist, build, node_module目录.
let fuf_file_exclude = '\v\~$|\.(o|exe|dll|bak|orig|swp|done)$|(^|[/\\])\.(hg|git|bzr)($|[/\\])'
let g:fuf_coveragefile_exclude = '\v\~$|\.(o|exe|dll|bak|orig|swp|class)$|(^|[/\\])\.(hg|git|bzr)($|[/\\])'

" fuf 文件搜索
let g:fuf_coveragefile_globPatterns = ['*.java', '*.js', '*.json', '*.xml', '*.yml', '*.yaml', '*.html', '*.ts', '*.css']
nmap <CS-R> :FufCoverageFile<CR>

" taglist
imap <C-o> :TlisgToggle<CR>
set tags=./tags;,tags 
