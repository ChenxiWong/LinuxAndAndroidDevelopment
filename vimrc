"Bundle
set nocompatible
syntax enable

filetype on
filetype off " Required

set rtp+=$HOME/.vim/bundle/vundle/
call vundle#rc()

" Use pathogen
"source ~/.vim/bundle/vim-pathogen/autoload/pathogen.vim
"call pathogen#runtime_append_all_bundles()

Bundle 'gmarik/vundle'
Bundle 'a.vim'
Bundle 'plasticboy/vim-markdown'
Bundle 'vim-autocomplete'
Bundle 'tagbar'
Bundle 'vim-powerline'
Bundle 'Colour-Sampler-Pack'
Bundle 'DoxygenToolkit'
Bundle 'nerdtree'
Bundle 'QFixToggle'
Bundle 'vim-headerGatesAdd'
Bundle 'OmniCppComplete'
Bundle 'snipmate'
Bundle 'vim-mru'
"Install syntastic
Bundle 'syntastic'

" Poweline
set laststatus=2
filetype on
let g:Powerline_symbols = 'unicode'
set t_Co=256
let g:solarized_termcolors=256
let g:Powerline_theme = 'default'
let g:Powerline_colorscheme = 'solarized256'
let g:Powerline_mode_n = 'NORMAL'

" HeaderGatesAdd
let g:DoxygenToolkit_authorName="wangchenxi"
let g:DoxygenToolkit_versionString="0.1.00"
let g:DoxygenToolkit_briefTag_funcName="yes"
function! SetTitle()
    call setline(1,"\#########################################################################")
    call append(line("."), "\# File Name: ".expand("%"))
    call append(line(".")+1, "\# Author: wangchenxi")
    call append(line(".")+2, "\# mail: chinawangchenxi@gmail.com")
    call append(line(".")+3, "\# Created Time: ".strftime("%c"))
    call append(line(".")+4, "\# brief:")
    call append(line(".")+5, "\#########################################################################")
    call append(line(".")+6, "\#!/bin/bash")
    call append(line(".")+7, "")
endfunction
function! SetMakefileTitle()
    call setline(1,"\#########################################################################")
    call append(line("."), "\# File Name: ".expand("%"))
    call append(line(".")+1, "\# Author: wangchenxi")
    call append(line(".")+2, "\# mail: chinawangchenxi@gmail.com")
    call append(line(".")+3, "\# Created Time: ".strftime("%c"))
    call append(line(".")+4, "\# brief:")
    call append(line(".")+5, "\#########################################################################")
    call append(line(".")+6, "")
endfunction

function! SetLuaTitle()
    call setline(1,"\-- ######################################################################")
    call append(line("."), "\-- File Name: ".expand("%"))
    call append(line(".")+1, "\-- Author: wangchenxi")
    call append(line(".")+2, "\-- mail: chinawangchenxi@gmail.com")
    call append(line(".")+3, "\-- Created Time: ".strftime("%c"))
    call append(line(".")+4, "\-- brief:")
    call append(line(".")+5, "\-- ######################################################################")
    call append(line(".")+6, "")
endfunction

function! SetProCTitle()
    call setline(1,"\/************************************************************************")
    call append(line("."), "\* File Name: ".expand("%"))
    call append(line(".")+1, "\* Author: wangchenxi")
    call append(line(".")+2, "\* mail: chinawangchenxi@gmail.com")
    call append(line(".")+3, "\* Created Time: ".strftime("%c"))
    call append(line(".")+4, "\* brief:")
    call append(line(".")+5, "\*************************************************************************")
    call append(line(".")+6, "")
endfunction

autocmd BufNewFile *.{py,x,h,hpp,c,cc,sqc,cpp} DoxAuthor
autocmd BufNewFile *.{sh} call SetTitle()
autocmd BufNewFile *.{lua} call SetLuaTitle()
autocmd BufNewFile {M,m}akefile call SetMakefileTitle()
autocmd BufNewFile *.{pc} call SetProCTitle()
function! AutoUpdateTheLastUpdateInfo()
        let s:original_pos = getpos(".")
        let s:regexp = "^[\\#]s*\\([#\\\"\\*]\\|\\/\\/\\)\\s\\?[lL]ast \\([uU]pdate\\|[cC]hange\\):"
        let s:lu = search(s:regexp)
        if s:lu != 0
                let s:update_str = matchstr(getline(s:lu), s:regexp)
                call setline(s:lu, s:update_str . strftime("%Y-%m-%d %H:%M:%S", localtime()))
                call setpos(".", s:original_pos)
        endif
endfunction
autocmd BufWritePost *.{py,sh,h,hpp,c,sqc,cpp,cc} call AutoUpdateTheLastUpdateInfo()
autocmd BufNewFile *.{h,hpp,c,sqc,cpp,cc} exec 'call append(0, "\/\/ Last Update:" . strftime("%Y-%m-%d %H:%M:%S", localtime()))'
autocmd BufNewFile  exec 'call append(0, "# Last Update:" . strftime("%Y-%m-%d %H:%M:%S", localtime()))'
autocmd BufNewFile {*.{py,sh},{M,m}akefile} exec 'call append(0, "# Last Update:" . strftime("%Y-%m-%d %H:%M:%S", localtime()))'
autocmd BufNewFile {*.lua} exec 'call append(0, "-- Last Update:" . strftime("%Y-%m-%d %H:%M:%S", localtime()))'
autocmd BufNewFile {*.{pc}} exec 'call append(0, "\/\* Last Update:" . strftime("%Y-%m-%d %H:%M:%S", localtime())." \*\/)'

" Color scheme
"colorscheme molokai
filetype plugin indent on

" Key Maps
set number
set smartindent
set cin
set ignorecase
" Userdefined indent style
set tabstop=4
set shiftwidth=4
set expandtab  "Uncomment when coding with Python
set list
set listchars=tab:\|\ ,nbsp:%,trail:-
set fileencodings=utf-8,gbk,ucs-bom,cp936,gb2312,gb18030
set incsearch
"set backup
set tags=/root/acl-master/tags

nnoremap <silent><F5> :NERDTreeToggle<CR>
nnoremap <silent><F6> :TagbarToggle<CR>
