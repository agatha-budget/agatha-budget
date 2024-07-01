# shell.nix
#
# Prepare a Nix shell with GINsim available.
#
# Use nix-shell or nix-shell shell.nix to start.

# Copyright 2020 by Sergiu Ivanov <sivanov@colimite.fr>
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

# Import nixpkgs under the local binding pkgs to be able to use stdenv
# and callPackage.
let
  pkgs = import <nixpkgs> {};
in
pkgs.stdenv.mkDerivation {
  name = "Agatha-shell";

  # Bring the package defined in ginsim.nix in scope.  buildInputs is
  # therefore a one-element list.  Its only element is the call of the
  # anonymous function defined in ginsim.nix.  I could have used let
  # to define a local binding ginsim = callPackage ./ginsim.nix {}; to
  # explicitly bind a name to this function call.
  #
  # callPackage can also be replaced by import.  In this case, the
  # arguments of the anonymous function defined in ginsim.nix get
  # their respective default values.
  buildInputs = [ (pkgs.callPackage ./back.nix {}) ];
}
